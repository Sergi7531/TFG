package com.example.moviez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    private List<Models.Film> films = new ArrayList<>();
    List<Integer> genresUser = new ArrayList<>();
    private RecyclerView recyclerForYou;
    private RecyclerView recyclerFriends;

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

        @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);


        forYou();

//        filmAdapter.notifyDataSetChanged();
    }

    private void hook(View view) {
        recyclerFriends = view.findViewById(R.id.recyclerFriends);
        recyclerForYou = view.findViewById(R.id.recyclerParaTi);
    }

    public void forYou() {
        films.clear();
        List<Models.Film> forYouFilms = new ArrayList<>();


        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                genresUser = (List<Integer>) documentSnapshot.get("genres");
            }

//            IMDB.api.getMoviesTopRated();


            films.addAll(forYouFilms);
        });
        adaptToRecycler(films, recyclerForYou);
    }

    private void adaptToRecycler(List<?> list, RecyclerView recyclerView) {
        System.out.println(list.size());
        recyclerView.setAdapter(new FilmAdapter((List<Models.Film>) list, requireContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    }


}