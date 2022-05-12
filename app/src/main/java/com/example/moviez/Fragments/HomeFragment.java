package com.example.moviez.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Activities.AppViewModel;
import com.example.moviez.Adapters.FilmAdapter;
import com.example.moviez.Adapters.MovieSearchResultAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * AAAAAAAAAAAAAAAAAA
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<Integer> genresUser = new ArrayList<>();
    private RecyclerView recyclerForYou;
    private RecyclerView recyclerFriends;

    public TextInputEditText searchInputUser;
    public RecyclerView recyclerViewUserSearch;

    public static String userId = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);
        forYou();

        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        recyclerViewUserSearch = getActivity().findViewById(R.id.recyclerMoviesSearch);

        searchInputUser.addTextChangedListener(
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
                                recyclerViewUserSearch.setAlpha(1f);
                                recyclerViewUserSearch.setAdapter(new MovieSearchResultAdapter(requireActivity(), moviesByQuery.results));
                                recyclerViewUserSearch.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                            }
                        });
                    }

                });
    }

    private void hook(View view) {
        recyclerFriends = view.findViewById(R.id.recyclerFriends);
        recyclerForYou = view.findViewById(R.id.recyclerParaTi);
        searchInputUser = view.findViewById(R.id.searchInputUsers);
        recyclerViewUserSearch = view.findViewById(R.id.recyclerMoviesSearch);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void forYou() {

        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                genresUser.addAll(documentSnapshot.toObject(Models.User.class).getFavoriteGenres());

                appViewModel.getMoviesForYou();

                appViewModel.forYouMovies.observe(getViewLifecycleOwner(), filmsByGenreForUser -> {
                    if (filmsByGenreForUser != null) {
                        recyclerForYou.setAlpha(1f);
                        recyclerForYou.setAdapter(new FilmAdapter(filmsByGenreForUser.results, requireActivity(), HomeFragment.this));
                        recyclerForYou.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
                    }
                });
            }
        });
    }
}