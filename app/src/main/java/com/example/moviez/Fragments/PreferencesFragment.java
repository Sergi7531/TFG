package com.example.moviez.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moviez.Activities.MainActivity;
import com.example.moviez.Adapters.GenreAdapter;
import com.example.moviez.Models.Genre;
import com.example.moviez.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PreferencesFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Genre> genres;
    private GridView listViewGenres;
    private LinearLayout linearSkip;

    private Button continueButton;
    private ImageView skip;

    public static List<Integer> selectedGenres = new ArrayList<>();

    public PreferencesFragment() { }

    public static PreferencesFragment newInstance(String param1, String param2) {
        PreferencesFragment fragment = new PreferencesFragment();
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
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);
        initData();
        GenreAdapter adapter = new GenreAdapter(getContext(), genres);
        listViewGenres.setAdapter(adapter);

        linearSkip.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        });

        skip.setOnClickListener(v ->  {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        });

        continueButton.setOnClickListener(v -> db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).update("favoriteGenres", selectedGenres).addOnSuccessListener(success -> {
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }));
    }

    private void hook(View view) {
        listViewGenres = view.findViewById(R.id.listViewGenres);
        linearSkip = view.findViewById(R.id.linearSkip);
        continueButton = view.findViewById(R.id.continueButton);
        skip = view.findViewById(R.id.skip);
    }

    private void initData() {
        genres = new ArrayList<>(Arrays.asList(new Genre (
                28,
                "Action",
                "https://i.imgur.com/tfdEAnz.jpeg"
        ), new Genre (
                12,
                "Adventure",
                "https://i.imgur.com/EUPXWvb.jpeg"
        ), new Genre (
                16,
                "Animation",
                "https://i.imgur.com/CHVJiYL.png"
        ), new Genre (
                35,
                "Comedy",
                "https://i.imgur.com/uqQn41U.jpeg"
        ), new Genre (
                80,
                "Crime",
                "https://i.imgur.com/GmuVmpa.jpeg"
        ), new Genre (
                99,
                "Documentary",
                "https://i.imgur.com/Fjzk0Ka.jpeg"
        ), new Genre (
                18,
                "Drama",
                "https://i.imgur.com/0gBcYaP.jpeg"
        ), new Genre (
                10751,
                "Family",
                "https://i.imgur.com/YEwVs1Y.jpeg"
        ), new Genre (
                14,
                "Fantasy",
                "https://i.imgur.com/gcYoXbA.jpeg"
        ), new Genre (
                36,
                "History",
                "https://i.imgur.com/xjdllMr.png"
        ), new Genre (
                27,
                "Horror",
                "https://i.imgur.com/0JpQkFU.jpeg"
        ), new Genre (
                10402,
                "Music",
                "https://i.imgur.com/F8bWJgE.jpeg"
        ), new Genre (
                9648,
                "Mystery",
                "https://i.imgur.com/FbKtb5a.jpeg"
        ), new Genre (
                10749,
                "Romance",
                "https://i.imgur.com/pNRN9fM.png"
        ), new Genre (
                878,
                "Science fiction",
                "https://i.imgur.com/r5HRKFM.jpeg"
        ), new Genre (
                10770,
                "TV Movie",
                "https://i.imgur.com/RmuFdPk.jpeg"
        ), new Genre (
                53,
                "Thriller",
                "https://i.imgur.com/UbJ45Fr.jpeg"
        ), new Genre (
                10752,
                "War",
                "https://i.imgur.com/Q0ADIKm.jpeg"
        ), new Genre (
                37,
                "Western",
                "https://i.imgur.com/duXc5At.jpeg"
        )));
    }
}