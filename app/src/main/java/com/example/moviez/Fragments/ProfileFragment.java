package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Adapters.FilmAdapter;
import com.example.moviez.Adapters.UserAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileFragment extends AppFragment {

    private TextView usuario;
    private TextView correo;
    private TextView followingNumber;
    private TextView followersNumber;
    private TextView watchedNumber;
    private TextView wantToWatchNumber;
    private TextView favoriteNumber;
    private TextView textNoMovies1;
    private TextView textNoMovies2;
    private TextView textNoMovies3;
    private TextView textNoUsers1;
    private TextView textNoUsers2;

    private ImageView profilepic;
    private RecyclerView recyclerLastViewed;
    private RecyclerView recyclerFavorites;
    private RecyclerView recyclerFollowing;
    private RecyclerView recyclerFollowers;
    private RecyclerView recyclerMoviesToWatch;

    private Button editarPerfil;
    private Button watchedButton;
    private Button toWatchButton;
    private Button favoritedButton;
    private Button followingButton;

    List<Models.Film> lastViewedFilms = new ArrayList<>();
    List<Models.Film> favoritedFilms = new ArrayList<>();
    List<Models.Film> toWatch = new ArrayList<>();
    List<Models.User> users = new ArrayList<>();
    List<Models.User> followers = new ArrayList<>();

    public static String userId = "";
    public static boolean isFollowing = false;
    public static boolean isOtherUser = false;

    public ProfileFragment() {
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public ProfileFragment(String userid) {
        userId = userid;
    }

    public static ProfileFragment newInstance(String param1) {
        ProfileFragment fragment = new ProfileFragment();
        if (!param1.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
            Bundle args = new Bundle();
            args.putString(param1, userId);
            fragment.setArguments(args);
        } else {
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);

        editarPerfil.setOnClickListener(v -> setFragment(new EditProfileFragment()));

        watchedButton.setOnClickListener(v -> setFragment(new MoviesFragment()));
        toWatchButton.setOnClickListener(v -> setFragment(new MoviesFragment()));
        favoritedButton.setOnClickListener(v -> setFragment(new MoviesFragment()));

        if (!userId.equals(Objects.requireNonNull(auth.getCurrentUser()).getUid())) {
            db.collection("users").document(auth.getCurrentUser().getUid())
                    .collection("following").addSnapshotListener((value, error) -> {
                if (value != null) {
                    isOtherUser = true;
                    if (value.isEmpty()) {
                        editarPerfil.setText(R.string.Seguir);
                        editarPerfil.setOnClickListener(v -> addToFollowing(userId));
                    } else {
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            if (Objects.requireNonNull(documentSnapshot.toObject(Models.User.class)).userid.equals(userId)) {
                                isFollowing = true;
                            } else {
                                editarPerfil.setText(R.string.Seguir);
                                editarPerfil.setOnClickListener(v -> addToFollowing(userId));
                            }

                            if (isFollowing) {
                                editarPerfil.setText(R.string.DejarDeSeguir);
                                editarPerfil.setOnClickListener(v -> unfollow(userId));
                            }

                        }
                    }
                }
            });
        } else {
            isOtherUser = false;
            editarPerfil.setText(R.string.EditarPerfil);
        }

        setUserDetails(userId);

        lastViewedFilms(userId);

        moviesToWatch(userId);

        favoriteFilms(userId);

        following(userId);

        followers(userId);
    }

    public void unfollow(String userId) {
        db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("following").document(userId).delete();
        db.collection("users").document(userId).collection("followers").document(auth.getCurrentUser().getUid()).delete();
        adaptUsersToRecycler(this.followers, recyclerFollowers);
        editarPerfil.setText(R.string.Seguir);
        editarPerfil.setOnClickListener(v -> {
            addToFollowing(userId);
            isFollowing = true;
        });
        followersNumber.setText(this.followers.size() + "");
    }

    public void addToFollowing(String userid) {

        db.collection("users").document(userid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Models.User usuarioASeguir = documentSnapshot.toObject(Models.User.class);

                if (usuarioASeguir != null) {
                    db.collection("users")
                            .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                            .collection("following").document(userid)
                            .set(usuarioASeguir);
                }

                db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).get().addOnSuccessListener(documentSnapshot1 -> db.collection("users")
                        .document(userId)
                        .collection("followers").document(auth.getCurrentUser().getUid())
                        .set(Objects.requireNonNull(documentSnapshot1.toObject(Models.User.class))));

                followersNumber.setText(this.followers.size() + "");
                editarPerfil.setOnClickListener(v -> {
                    unfollow(userId);
                    isFollowing = false;
                });
            }
        });
        editarPerfil.setText(R.string.DejarDeSeguir);
    }

    public void following(String userId) {

        db.collection("users").document(userId).collection("following").addSnapshotListener((value, error) -> {
            this.users.clear();
            if (value != null) {
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    this.users.add(documentSnapshot.toObject(Models.User.class));
                }
            }
            adaptUsersToRecycler(users, recyclerFollowing);
            checkVoidList(users, followingButton, textNoUsers2, getString(R.string.No_Sigues));
            followingNumber.setText(this.users.size() + "");
        });

    }

    public void followers(String userId) {

        db.collection("users").document(userId).collection("followers").addSnapshotListener((value, error) -> {
            this.followers.clear();
            if (value != null) {
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    this.followers.add(documentSnapshot.toObject(Models.User.class));
                }
            }

            adaptUsersToRecycler(this.followers, recyclerFollowers);
            if (this.followers.isEmpty()) {
                textNoUsers1.setAlpha(1);
                if (isOtherUser) {
                    textNoUsers1.setText(getString(R.string.No_Seguidores));
                } else {
                    textNoUsers1.setText(getString(R.string.No_Te_Siguen));
                }
            } else {
                textNoUsers1.setAlpha(0);
            }
            followersNumber.setText(this.followers.size() + "");
        });

    }

    public void lastViewedFilms(String userId) {
        lastViewedFilms.clear();
        List<Models.Film> favoriteFilms = new ArrayList<>();
        db.collection("users").document(userId).collection("watchedFilms").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                favoriteFilms.add(documentSnapshot.toObject(Models.Film.class));
            }
            lastViewedFilms.addAll(favoriteFilms);
            adaptFilmsToRecycler(lastViewedFilms, recyclerLastViewed);
            watchedNumber.setText(String.valueOf(lastViewedFilms.size()));
            checkVoidList(lastViewedFilms, watchedButton, textNoMovies1, getString(R.string.No_Peliculas));
        });
    }

    public void moviesToWatch(String userId) {
        toWatch.clear();
        List<Models.Film> moviesToWatch = new ArrayList<>();
        db.collection("users").document(userId).collection("moviesToWatch").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                moviesToWatch.add(documentSnapshot.toObject(Models.Film.class));
            }
            toWatch.addAll(moviesToWatch);
            adaptFilmsToRecycler(toWatch, recyclerMoviesToWatch);
            wantToWatchNumber.setText(String.valueOf(toWatch.size()));
            checkVoidList(toWatch, toWatchButton, textNoMovies2, getString(R.string.No_Peliculas));
        });
    }

    public void favoriteFilms(String userId) {
        favoritedFilms.clear();
        List<Models.Film> favoriteFilms = new ArrayList<>();
        db.collection("users").document(userId).collection("favoritedFilms").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                favoriteFilms.add(documentSnapshot.toObject(Models.Film.class));
            }
            favoritedFilms.addAll(favoriteFilms);
            adaptFilmsToRecycler(favoritedFilms, recyclerFavorites);
            favoriteNumber.setText(String.valueOf(favoritedFilms.size()));
            checkVoidList(favoritedFilms, favoritedButton, textNoMovies3, getString(R.string.No_Peliculas));
        });
    }

    private void checkVoidList(List<?> films, Button button, TextView textView, String texto) {


        if (films.isEmpty()) {
            button.setAlpha(0f);
             if (isOtherUser) {
                 textView.setAlpha(1);
                 textView.setText(texto);
             } else {
                textView.setAlpha(0);
                button.setAlpha(1);
                button.setEnabled(true);
                button.setClickable(true);
                button.setText("+");
             }
        } else {
            textView.setAlpha(0);
            button.setAlpha(0f);
            button.setEnabled(false);
            button.setClickable(false);
        }
    }

    private void adaptFilmsToRecycler(List<?> list, RecyclerView recyclerView) {
        recyclerView.setAdapter(new FilmAdapter((List<Models.Film>) list, requireContext(), ProfileFragment.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    }

    private void adaptUsersToRecycler(List<?> list, RecyclerView recyclerView) {
        recyclerView.setAdapter(new UserAdapter((List<Models.User>) list, requireContext(), ProfileFragment.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
    }

    private void hook(View view) {
        /*
        TextView textNoMovies1;
    private TextView textNoMovies2;
    private TextView textNoMovies3;
    private TextView textNoUsers1;
    private TextView textNoUsers2;
         */
        usuario = view.findViewById(R.id.usuario);
        correo = view.findViewById(R.id.correo);
        editarPerfil = view.findViewById(R.id.editarPerfil);
        profilepic = view.findViewById(R.id.profilePicture);
        recyclerLastViewed = view.findViewById(R.id.recyclerWatchedMovies);
        recyclerFavorites = view.findViewById(R.id.recyclerFavoritedMovies);
        recyclerFollowing = view.findViewById(R.id.recyclerFollowedUsers);
        recyclerFollowers = view.findViewById(R.id.recyclerFollowers);
        recyclerMoviesToWatch = view.findViewById(R.id.recyclerMoviesToWatch);
        followingNumber = view.findViewById(R.id.followingNumber);
        followersNumber = view.findViewById(R.id.followersNumber);
        watchedNumber = view.findViewById(R.id.watchedNumber);
        wantToWatchNumber = view.findViewById(R.id.wantToWatchNumber);
        favoriteNumber = view.findViewById(R.id.favoriteNumber);
        watchedButton = view.findViewById(R.id.watchedButton);
        toWatchButton = view.findViewById(R.id.toWatchButton);
        favoritedButton = view.findViewById(R.id.favoritedButton);
        followingButton = view.findViewById(R.id.followingButton);
        textNoMovies1 = view.findViewById(R.id.textNoMovies1);
        textNoMovies2 = view.findViewById(R.id.textNoMovies2);
        textNoMovies3 = view.findViewById(R.id.textNoMovies3);
        textNoUsers1 = view.findViewById(R.id.textNoUsers1);
        textNoUsers2 = view.findViewById(R.id.textNoUsers2);
    }

    private void setUserDetails(String userid) {
        db.collection("users").document(userid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Models.User user = documentSnapshot.toObject(Models.User.class);
                if (user != null) {
                    usuario.setText(user.username);
                }
                if (user != null) {
                    correo.setText(user.email);
                }
                if (user != null && !user.profileImageURL.equals("")) {
                    Glide.with(getActivity()).load(user.profileImageURL).circleCrop().into(profilepic);
                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .addToBackStack(ProfileFragment.class.getSimpleName())
                    .commit();
        }
    }
}