package com.example.moviez.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Activities.AppViewModel;
import com.example.moviez.Adapters.FilmAdapter;
import com.example.moviez.Adapters.MovieSearchResultAdapter;
import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;

public class MoviesFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public RecyclerView recyclerUpcomingMovies;
    public RecyclerView recyclerMoviesInCinemas;
    public RecyclerView recyclerMoviesSearch;
    public TextInputEditText searchInputFilm;

    public MoviesFragment() {
    }

    public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
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
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerUpcomingMovies = (RecyclerView) getActivity().findViewById(R.id.recyclerUpcomingMovies);
        recyclerMoviesInCinemas = getActivity().findViewById(R.id.recyclerMoviesInCinemas);
        recyclerMoviesSearch = getActivity().findViewById(R.id.recyclerViewMovieSearch);
        searchInputFilm = getActivity().findViewById(R.id.searchInputFilm);

        searchInputFilm.addTextChangedListener(
                new TextWatcher() {
                       @Override
                       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                           recyclerMoviesSearch.setAlpha(0f);
                       }

                       @Override
                       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                           recyclerMoviesSearch.setAlpha(1f);
                           AppViewModel.searchMoviesByQuery(charSequence.toString());
                           AppViewModel.moviesByQuery.observe(getViewLifecycleOwner(), moviesByQuery -> {

                               recyclerMoviesSearch.setAdapter(new MovieSearchResultAdapter(requireActivity(), moviesByQuery.results, MoviesFragment.this));
                               recyclerMoviesSearch.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                               if (charSequence.toString().isEmpty()) {
                                   recyclerMoviesSearch.setAlpha(0f);
                               }
                               else {
                                   recyclerMoviesSearch.setAlpha(1f);
                               }
                           });
                       }

                       @Override
                       public void afterTextChanged(Editable editable) {
                       }

            });

        AppViewModel.getUpcomingMovies();
        AppViewModel.getActualCinemaMovies();

        AppViewModel.upcomingMoviesResponse.observe(getViewLifecycleOwner(), upcomingMoviesResponse -> {
            if (upcomingMoviesResponse != null) {
                recyclerUpcomingMovies.setAdapter(new FilmAdapter(upcomingMoviesResponse.results, requireContext(), MoviesFragment.this));
                recyclerUpcomingMovies.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            }
        });

        AppViewModel.actualMoviesInCinemaResponse.observe(getViewLifecycleOwner(), actualMoviesResponse -> {
            if (actualMoviesResponse != null) {
                recyclerMoviesInCinemas.setAdapter(new FilmAdapter(actualMoviesResponse.results, requireContext(), MoviesFragment.this));
                recyclerMoviesInCinemas.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            }
        });
    }
}