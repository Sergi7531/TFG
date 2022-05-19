package com.example.moviez.Fragments;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.moviez.R;

public class AnimationFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ImageView logoAnimation;

    public AnimationFragment() {

    }

    public static AnimationFragment newInstance(String param1, String param2) {
        AnimationFragment fragment = new AnimationFragment();
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
        return inflater.inflate(R.layout.fragment_animation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoAnimation = view.findViewById(R.id.logoAnimation);
        String sImage;
        AnimationDrawable animation = new AnimationDrawable();
        for (int i = 0; i <= 89; i++) {
            sImage = "moviez_gif_";
            animation.addFrame(
                    ResourcesCompat.getDrawable(
                            getResources(), getResources().getIdentifier(
                                    sImage + i, "drawable", requireContext().getPackageName()),
                            null),
                    20);
        }
        animation.setOneShot(true);
        logoAnimation.setImageDrawable(animation);
        animation.start();

    }
}