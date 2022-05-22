package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class NewCommentFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static int filmId = 0;

    private TextInputEditText commentText;
    public Button publishButton;
    public CheckBox spoilerCheckBox;
    public ImageView backButton;

    public RatingBar userRatingBar;
    public TextView actualRating;
    private float starsf = 0.0f;

    public NewCommentFragment() { }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);

        backButton.setOnClickListener(view1 -> getActivity().getSupportFragmentManager().popBackStack());

        userRatingBar.setRating(5);

        userRatingBar.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                float touchPositionX = event.getX();
                float width = userRatingBar.getWidth();
                starsf = (touchPositionX / width) * 5.0f;
//                    Set stars to 1 decimal place:
                starsf = (float) Math.round(starsf * 10) / 10;
                if(starsf <=5 && starsf >= 1) {
                    userRatingBar.setRating(starsf);
                    actualRating.setText(String.valueOf(starsf));
                    v.setPressed(false);
                }
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setPressed(true);
            }

            if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setPressed(false);
            }
            return true;
        });

        publishButton.setOnClickListener(view1 -> {
            if(!Objects.requireNonNull(commentText.getText()).toString().isEmpty() && userRatingBar.getRating() != 0) {
                String comment = commentText.getText().toString();
//                Get the rating from the rating bar (1 decimal place):
                float rating = (float) Math.round(userRatingBar.getRating() * 10) / 10;

                boolean spoiler = spoilerCheckBox.isChecked();

                if(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl() != null) {
                    db.collection("comments").document(String.valueOf(filmId)).set(new Models.Film(filmId));
                    db.collection("comments").document(String.valueOf(filmId)).collection("comments").document(auth.getCurrentUser().getUid()).set(new Models.Comment(auth.getCurrentUser().getUid(), comment, Objects.requireNonNull(auth.getCurrentUser().getPhotoUrl()).toString(), auth.getCurrentUser().getDisplayName(), rating, spoiler));
                } else {
                    db.collection("comments").document(String.valueOf(filmId)).set(new Models.Film(filmId));
                    db.collection("comments").document(String.valueOf(filmId)).collection("comments").document(auth.getCurrentUser().getUid()).set(new Models.Comment(auth.getCurrentUser().getUid(), comment, "", auth.getCurrentUser().getDisplayName(), rating, spoiler));
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
        backButton = view.findViewById(R.id.backButton4);
    }

}