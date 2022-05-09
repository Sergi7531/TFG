package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewCommentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewCommentFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private static int filmId = 0;

    private int mParam1;
    private String mParam2;
    private TextInputEditText commentText;
    public Button publishButton;
    public CheckBox spoilerCheckBox;

    public RatingBar userRatingBar;
    public TextView actualRating;

    public NewCommentFragment() {
        // Required empty public constructor
    }

//    Constructor for the fragment with filmId:

    public NewCommentFragment(int mParam1) {
        filmId = mParam1;
    }

    public static NewCommentFragment newInstance(String param1, String param2) {
        NewCommentFragment fragment = new NewCommentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_comment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);

//        Upload comment to firebase and go back to the movie page:


        userRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    float touchPositionX = event.getX();
                    float width = userRatingBar.getWidth();
                    float starsf = (touchPositionX / width) * 5.0f;
                    int stars = (int)starsf + 1;
                    userRatingBar.setRating(stars);
                    actualRating.setText(String.valueOf(stars));
                    v.setPressed(false);
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setPressed(true);
                }

                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    v.setPressed(false);
                }
                return true;
            }
        });

        publishButton.setOnClickListener(view1 -> {
            if(!commentText.getText().toString().isEmpty() && userRatingBar.getRating() != 0) {
                String comment = commentText.getText().toString();
                float rating = userRatingBar.getNumStars();
                String ratingString = String.valueOf(rating);

                boolean spoiler = spoilerCheckBox.isChecked();

                if(auth.getCurrentUser().getPhotoUrl() != null) {
                    db.collection("comments").document(String.valueOf(filmId)).collection("comments").add(new Models.Comment(comment, auth.getCurrentUser().getPhotoUrl().toString(), auth.getCurrentUser().getDisplayName(), Double.parseDouble(ratingString), spoiler));
                    System.out.println("Coment added: " + filmId);
                } else {
                    db.collection("comments").document(String.valueOf(filmId)).collection("comments").add(new Models.Comment(comment, "", auth.getCurrentUser().getDisplayName(), Double.parseDouble(ratingString), spoiler));
                    System.out.println("Coment added without imageUrl: " + filmId);
                }

                //                Go back to the movie page:
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }

    public void hook(View view) {
        commentText = view.findViewById(R.id.commentText);
        userRatingBar = view.findViewById(R.id.userRatingBar);
        actualRating = view.findViewById(R.id.actualRating);
        publishButton = view.findViewById(R.id.publishButton);
        spoilerCheckBox = view.findViewById(R.id.spoilerCheckBox);
    }

}