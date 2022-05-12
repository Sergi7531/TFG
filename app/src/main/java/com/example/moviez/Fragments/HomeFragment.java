package com.example.moviez.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Adapters.FilmAdapter;
import com.example.moviez.Adapters.UserSearchResultAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends AppFragment {


    List<Integer> genresUser = new ArrayList<>();
    private RecyclerView recyclerForYou;
    private RecyclerView recyclerFriends;

    public List<Models.User> users = new ArrayList<>();
    public TextInputEditText searchInputUser;
    public RecyclerView recyclerViewUserSearch;

    public static String userId = "";
    private UserSearchResultAdapter adapter;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);
        forYou();

        recyclerViewUserSearch = view.findViewById(R.id.recyclerViewUserSearch);

        recyclerViewUserSearch.setAdapter(adapter = new UserSearchResultAdapter(requireActivity(), users, HomeFragment.this));
        recyclerViewUserSearch.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));

        searchInputUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerViewUserSearch.setAlpha(0f);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               recyclerViewUserSearch.setAlpha(1f);
               System.out.println("TEXT CHANGED");
               firebaseUserSearch(charSequence.toString());
               if (charSequence.toString().isEmpty()) {
                   recyclerViewUserSearch.setAlpha(0f);
               }
               else {
                   recyclerViewUserSearch.setAlpha(1f);
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void firebaseUserSearch(String query) {

        users.clear();

        System.out.println("BUSCANDO: " + query);
        db.collection("users").orderBy("username").startAt(query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    System.out.println("FIN CONSULTA");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Models.User user = document.toObject(Models.User.class);
                        if (user.username.contains(query)) {
                            users.add(user);
                            System.out.println("ENCONTRADO!: " + user.username);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("Not found");
                }

            }
        });
    }


    private void hook(View view) {
        recyclerFriends = view.findViewById(R.id.recyclerFriends);
        recyclerForYou = view.findViewById(R.id.recyclerParaTi);
        searchInputUser = view.findViewById(R.id.searchInputUsers);
        recyclerViewUserSearch = view.findViewById(R.id.recyclerViewUserSearch);
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