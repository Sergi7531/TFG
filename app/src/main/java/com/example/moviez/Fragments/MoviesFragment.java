package com.example.moviez.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Activities.AppViewModel;
import com.example.moviez.Adapters.FilmAdapter;
import com.example.moviez.Adapters.MovieSearchResultAdapter;
import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public RecyclerView recyclerUpcomingMovies;
    public RecyclerView recyclerMoviesInCinemas;
    public RecyclerView recyclerMoviesSearch;
    public TextInputEditText searchInputFilm;

    int internPage = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoviesFragment() {
        // Required empty public constructor
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerUpcomingMovies = (RecyclerView) getActivity().findViewById(R.id.recyclerUpcomingMovies);
        recyclerMoviesInCinemas = getActivity().findViewById(R.id.recyclerMoviesInCinemas);
        recyclerMoviesSearch = getActivity().findViewById(R.id.recyclerMoviesSearch);
        searchInputFilm = getActivity().findViewById(R.id.searchInputFilm);

        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

//        Search bar:


        searchInputFilm.addTextChangedListener(
                new TextWatcher() {
                       @Override
                       public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                       }

                       @Override
                       public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                           viewModel.searchMoviesByQuery(charSequence.toString());
                       }

                       @Override
                       public void afterTextChanged(Editable editable) {
                           viewModel.moviesByQuery.observe(getViewLifecycleOwner(), moviesByQuery -> {
                               if (moviesByQuery != null) {
                                   recyclerMoviesSearch.setAlpha(1f);
                                   recyclerMoviesSearch.setAdapter(new MovieSearchResultAdapter(requireActivity(), moviesByQuery.results));
                                   recyclerMoviesSearch.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                               }
                           });
                       }

            });

        viewModel.getUpcomingMovies();
        viewModel.getActualCinemaMovies();

        viewModel.upcomingMoviesResponse.observe(getViewLifecycleOwner(), upcomingMoviesResponse -> {
            if (upcomingMoviesResponse != null) {
                recyclerUpcomingMovies.setAdapter(new FilmAdapter(upcomingMoviesResponse.results, requireContext(), MoviesFragment.this));
                recyclerUpcomingMovies.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            }
        });

        viewModel.actualMoviesInCinemaResponse.observe(getViewLifecycleOwner(), actualMoviesResponse -> {
            if (actualMoviesResponse != null) {
                recyclerMoviesInCinemas.setAdapter(new FilmAdapter(actualMoviesResponse.results, requireContext(), MoviesFragment.this));
                recyclerMoviesInCinemas.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            }
        });
    }
}