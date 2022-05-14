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
import com.example.moviez.Models;
import com.example.moviez.R;
import com.example.moviez.Adapters.UserAdapter;
import com.example.moviez.UpdateFilmsInCinemas;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends AppFragment {

    private TextView usuario;
    private TextView correo;
    private TextView followingNumber;
    private TextView followersNumber;
    private TextView watchedNumber;
    private TextView wantToWatchNumber;
    private TextView favoriteNumber;

    private ImageView profilepic;
    private RecyclerView recyclerLastViewed;
    private RecyclerView recyclerFavorites;
    private RecyclerView recyclerFollowing;
    private RecyclerView recyclerFollowers;
    private RecyclerView recyclerMoviesToWatch;

    private Button editarPerfil;

    List<Models.Film> lastViewedFilms = new ArrayList<>();
    List<Models.Film> favoritedFilms = new ArrayList<>();
    List<Models.Film> toWatch = new ArrayList<>();
    List<Models.User> users = new ArrayList<>();
    List<Models.User> followers = new ArrayList<>();

    public static String userId = "";
    public static boolean isFollowing = false;

    public ProfileFragment() {
        // Required empty public constructor
        this.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public ProfileFragment(String userid) {
        // Required empty public constructor
        this.userId = userid;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1) {
        ProfileFragment fragment = new ProfileFragment();
        if(param1 != FirebaseAuth.getInstance().getCurrentUser().getUid()) {
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

        editarPerfil.setOnClickListener(v -> {
            setFragment(new EditProfileFragment());
        });

        if(userId != auth.getCurrentUser().getUid()) {
            db.collection("users").document(auth.getCurrentUser().getUid())
                    .collection("following").get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            if (documentSnapshot.toObject(Models.User.class).userid.equals(userId)) {
                                isFollowing = true;
                            }
                            else {
                                editarPerfil.setText("Seguir");
                                editarPerfil.setOnClickListener(v -> {
                                    addToFollowing(userId);
                                });
                            }

                            if (isFollowing) {
                                editarPerfil.setText("Dejar de seguir");
                                editarPerfil.setOnClickListener(v -> {
                                    unfollow(userId);
                                    isFollowing = false;
                                });
                            }

                        }
            });

        } else {
            editarPerfil.setText("Editar perfil");
        }

        UpdateFilmsInCinemas.initDates();

        setUserDetails(userId);

        lastViewedFilms(userId);

        moviesToWatch(userId);

        favoriteFilms(userId);

        following(userId);

        followers(userId);
    }

    public void unfollow(String userId) {
        db.collection("users").document(auth.getCurrentUser().getUid())
                .collection("following").document(userId).delete();
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
        });
    }

    public void addToFollowing(String userid) {
        db.collection("users").document(userid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Models.User usuarioASeguir = documentSnapshot.toObject(Models.User.class);

                db.collection("users")
                        .document(auth.getCurrentUser().getUid())
                        .collection("following").document(userid)
                        .set(usuarioASeguir);

                db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot1 -> {
                    db.collection("users")
                            .document(userId)
                            .collection("followers").document(auth.getCurrentUser().getUid())
                            .set(documentSnapshot1.toObject(Models.User.class));
                });

                following(userId);

                followersNumber.setText(this.followers.size() + "");
            }
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
        });
    }

    public void following(String userId) {
        users.clear();
        List<Models.User> following = new ArrayList<>();

        db.collection("users").document(userId).collection("following").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                following.add(documentSnapshot.toObject(Models.User.class));
            }
            users.addAll(following);
            adaptUsersToRecycler(users, recyclerFollowing);
            followingNumber.setText(users.size() + "");
        });

    }

    public void followers(String userId) {
        followers.clear();
        List<Models.User> followers = new ArrayList<>();

        db.collection("users").document(userId).collection("followers").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                followers.add(documentSnapshot.toObject(Models.User.class));
            }
            this.followers.addAll(followers);
            adaptUsersToRecycler(this.followers, recyclerFollowers);
            followersNumber.setText(this.followers.size() + "");
        });

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
    }

    private void setUserDetails(String userid) {
        db.collection("users").document(userid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Models.User user = documentSnapshot.toObject(Models.User.class);
                usuario.setText(user.username);
                correo.setText(user.email);
                if (user.profileImageURL != null) {
                    Glide.with(getActivity()).load(user.profileImageURL).circleCrop().into(profilepic);
                } else {
                    profilepic.setImageResource(R.drawable.ic_baseline_person_24);
                }
            }
        });

    }
    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }




}