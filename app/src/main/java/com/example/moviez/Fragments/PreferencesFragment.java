package com.example.moviez.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Models.Genre;
import com.example.moviez.Adapters.GenreAdapter;
import com.example.moviez.Activities.MainActivity;
import com.example.moviez.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PreferencesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PreferencesFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Genre> genres;
    private RecyclerView genresRecycler;
    private LinearLayout linearSkip;

    private Button continueButton;

    public static List<Integer> selectedGenres = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PreferencesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PreferencesFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_preferences, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);
        initData();
        GenreAdapter adapter = new GenreAdapter(getContext(), genres);
        genresRecycler.setAdapter(adapter);
        genresRecycler.setLayoutManager(new GridLayoutManager(requireContext(), 2,
                RecyclerView.VERTICAL, false));

        linearSkip.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            startActivity(intent);
        });

        continueButton.setOnClickListener(v -> {
                db.collection("users").document(auth.getCurrentUser().getUid()).update("favoriteGenres", selectedGenres).addOnSuccessListener(success -> {
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
            });
        });


    }

    private void hook(View view) {
        genresRecycler = view.findViewById(R.id.recyclerGenres);
        linearSkip = view.findViewById(R.id.linearSkip);
        continueButton = view.findViewById(R.id.continueButton);
    }


    private void initData() {
        genres = new ArrayList<>(Arrays.asList(new Genre (
                28,
                "Action"
        ), new Genre (
                12,
                "Adventure"
        ), new Genre (
                16,
                "Animation"
        ), new Genre (
                35,
                "Comedy"
        ), new Genre (
                80,
                "Crime"
        ), new Genre (
                99,
                "Documentary"
        ), new Genre (
                18,
                "Drama"
        ), new Genre (
                10751,
                "Family"
        ), new Genre (
                14,
                "Fantasy"
        ), new Genre (
                36,
                "History"
        ), new Genre (
                27,
                "Horror"
        ), new Genre (
                10402,
                "Music"
        ), new Genre (
                9648,
                "Mystery"
        ), new Genre (
                10749,
                "Romance"
        ), new Genre (
                878,
                "Science fiction"
        ), new Genre (
                10770,
                "TV Movie"
        ), new Genre (
                53,
                "Thriller"
        ), new Genre (
                10752,
                "War"
        ), new Genre (
                37,
                "Western"
        )));
    }
}