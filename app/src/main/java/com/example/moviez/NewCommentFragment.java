package com.example.moviez;

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

    public NewCommentFragment(int param1) {
        filmId = mParam1;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewCommentFragment.
     */
    // TODO: Rename and change types and number of parameters
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
                                             public boolean onTouch(View view, MotionEvent motionEvent) {
                                                 if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                                                     actualRating.setText(String.valueOf(userRatingBar.getRating()));
                                                     System.out.println(userRatingBar.getRating());
                                                     return true;
                                                 }
                                                 return false;
                                             }
                                         });

        if(!commentText.getText().toString().isEmpty() && userRatingBar.getRating() != 0) {
            publishButton.setOnClickListener(view1 -> {
                String comment = commentText.getText().toString();
                float rating = userRatingBar.getNumStars();
                String ratingString = String.valueOf(rating);

                boolean spoiler = spoilerCheckBox.isChecked();

                db.collection("films").document(String.valueOf(filmId)).collection("comments").add(new Models.Comment(comment, auth.getCurrentUser().getPhotoUrl().toString(), auth.getCurrentUser().getDisplayName(), Double.parseDouble(ratingString), spoiler));
//                Go back to the movie page:
                getActivity().getSupportFragmentManager().popBackStack();
            });
        }

    }

    public void hook(View view) {
        commentText = view.findViewById(R.id.commentText);
        userRatingBar = view.findViewById(R.id.userRatingBar);
        actualRating = view.findViewById(R.id.actualRating);
        publishButton = view.findViewById(R.id.publishButton);
        spoilerCheckBox = view.findViewById(R.id.spoilerCheckBox);
    }

}