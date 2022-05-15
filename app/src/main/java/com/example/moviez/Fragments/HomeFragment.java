package com.example.moviez.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Adapters.CinemaAdapter;
import com.example.moviez.Adapters.FilmAdapter;
import com.example.moviez.Adapters.UserActivityAdapter;
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
    public List<Models.UserActivity> userActivities = new ArrayList<>();
    public TextInputEditText searchInputUser;
    public RecyclerView recyclerViewUserSearch;
    public RecyclerView recyclerCinemas;
    public ProgressBar animacionCarga;

    public static String userId = "";
    private UserSearchResultAdapter adapter;
    public UserActivityAdapter userActivityAdapter;

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
                recyclerViewUserSearch.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               recyclerViewUserSearch.setVisibility(View.VISIBLE);
               System.out.println("TEXT CHANGED");
               firebaseUserSearch(charSequence.toString());
               if (charSequence.toString().isEmpty()) {
                   recyclerViewUserSearch.setVisibility(View.GONE);
               }
               else {
                   recyclerViewUserSearch.setVisibility(View.VISIBLE);
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recyclerFriends = view.findViewById(R.id.recyclerFriends);

        List<Models.User> following = new ArrayList<>();
        List<Models.UserActivity> userActivities = new ArrayList<>();

//        Get all the users that the current user is following:

        db.collection("users").document(auth.getCurrentUser().getUid()).collection("following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        following.add(document.toObject(Models.User.class));
                    }
                }
            }
        });

//        Get all the favoritedFilms of the users that the current user is following:

        db.collection("users").document(auth.getCurrentUser().getUid()).collection("following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("users").document(document.getId()).collection("favoritedFilms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentActivity : task.getResult()) {
                                        Models.UserActivity userActivity = new Models.UserActivity();
                                        userActivity.movieImage = documentActivity.toObject(Models.Film.class).poster_path;
                                        userActivity.userImage = document.toObject(Models.User.class).profileImageURL;
                                        userActivity.username = document.toObject(Models.User.class).username;
                                        userActivity.movieName = documentActivity.toObject(Models.Film.class).title;
                                        userActivity.movieId = documentActivity.toObject(Models.Film.class).id;
                                        userActivity.textToShow = "El usuario " + userActivity.username + " ha marcado como favorita " + userActivity.movieName;
                                        userActivities.add(userActivity);
                                    }
                                }
                            }
                        });

                        db.collection("users").document(document.getId()).collection("watchedFilms").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentActivity : task.getResult()) {
                                        Models.UserActivity userActivity = new Models.UserActivity();
                                        userActivity.movieImage = documentActivity.toObject(Models.Film.class).poster_path;
                                        userActivity.userImage = document.toObject(Models.User.class).profileImageURL;
                                        userActivity.username = document.toObject(Models.User.class).username;
                                        userActivity.movieName = documentActivity.toObject(Models.Film.class).title;
                                        userActivity.movieId = documentActivity.toObject(Models.Film.class).id;
                                        userActivity.textToShow = "El usuario " +userActivity.username + " ha visto la película " + userActivity.movieName;
                                        userActivities.add(userActivity);
                                        recyclerFriends.setAdapter(userActivityAdapter = new UserActivityAdapter(userActivities, requireActivity(), HomeFragment.this));
                                        recyclerFriends.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });

//        get the cinemas collection:

        List<Models.Cinema> cinemas = new ArrayList<>();

        db.collection("cinemas").get().addOnCompleteListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isSuccessful()) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots.getResult()) {
                    cinemas.add(document.toObject(Models.Cinema.class));
                }
                recyclerCinemas.setAdapter(new CinemaAdapter(cinemas, requireActivity(), HomeFragment.this));
                recyclerCinemas.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            }
                });







        }

    private void firebaseUserSearch(String query) {

        users.clear();

        db.collection("users").orderBy("username").startAt(query).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Models.User user = document.toObject(Models.User.class);
                        if (user.username.contains(query)) {
                            users.add(user);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    private void hook(View view) {
        recyclerFriends = view.findViewById(R.id.recyclerFriends);
        recyclerForYou = view.findViewById(R.id.recyclerParaTi);
        searchInputUser = view.findViewById(R.id.searchInputUsers);
        recyclerViewUserSearch = view.findViewById(R.id.recyclerViewUserSearch);
        recyclerCinemas = view.findViewById(R.id.recyclerCinemas);
        animacionCarga = view.findViewById(R.id.animacionCarga);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void forYou() {

        db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                genresUser.addAll(documentSnapshot.toObject(Models.User.class).getFavoriteGenres());

                appViewModel.getMoviesForYou();

                appViewModel.forYouMovies.observe(getViewLifecycleOwner(), filmsByGenreForUser -> {
                    if (filmsByGenreForUser != null) {
                        recyclerForYou.setAdapter(new FilmAdapter(filmsByGenreForUser.results, requireActivity(), this));
                        recyclerForYou.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
                        animacionCarga.setAlpha(0f);
                        recyclerForYou.setAlpha(1f);
                    }
                });
            }
        });
    }
}