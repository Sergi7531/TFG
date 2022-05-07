package com.example.moviez;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);

        editarPerfil.setOnClickListener(v -> {
            setFragment(new EditProfileFragment());
        });

        setUserDetails();

        lastViewedFilms();

        moviesToWatch();

        favoriteFilms();

        following();

        followers();
    }

    public void lastViewedFilms() {
        lastViewedFilms.clear();
        List<Models.Film> favoriteFilms = new ArrayList<>();
        db.collection("users").document(auth.getCurrentUser().getUid()).collection("lastViewedFilms").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                favoriteFilms.add(documentSnapshot.toObject(Models.Film.class));
            }
            lastViewedFilms.addAll(favoriteFilms);
            adaptFilmsToRecycler(lastViewedFilms, recyclerLastViewed);
        });
    }

    public void moviesToWatch() {
        toWatch.clear();
        List<Models.Film> moviesToWatch = new ArrayList<>();
        db.collection("users").document(auth.getCurrentUser().getUid()).collection("moviesToWatch").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                moviesToWatch.add(documentSnapshot.toObject(Models.Film.class));
            }
            toWatch.addAll(moviesToWatch);
        });
        adaptFilmsToRecycler(toWatch, recyclerMoviesToWatch);
    }

    public void favoriteFilms() {
        favoritedFilms.clear();
        List<Models.Film> favoriteFilms = new ArrayList<>();
        db.collection("users").document(auth.getCurrentUser().getUid()).collection("favoritedFilms").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                favoriteFilms.add(documentSnapshot.toObject(Models.Film.class));
            }
            favoritedFilms.addAll(favoriteFilms);
            adaptFilmsToRecycler(favoritedFilms, recyclerFavorites);
        });
    }

    public void following() {
        users.clear();
        List<Models.User> following = new ArrayList<>();

        db.collection("users").document(auth.getCurrentUser().getUid()).collection("following").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                following.add(documentSnapshot.toObject(Models.User.class));
            }
            users.addAll(following);
            adaptUsersToRecycler(users, recyclerFollowing);
            followingNumber.setText(users.size() + "");
        });

    }

    public void followers() {
        followers.clear();
        List<Models.User> followers = new ArrayList<>();

        db.collection("users").document(auth.getCurrentUser().getUid()).collection("followers").get().addOnSuccessListener(queryDocumentSnapshots -> {
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
        recyclerView.setAdapter(new UserAdapter((List<Models.User>) list, requireContext()));
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
    }

    private void setUserDetails() {
        usuario.setText(auth.getCurrentUser().getDisplayName());
        correo.setText(auth.getCurrentUser().getEmail());
        if (auth.getCurrentUser().getPhotoUrl() != null) {
            Glide.with(getActivity()).load(auth.getCurrentUser().getPhotoUrl()).circleCrop().into(profilepic);
        } else {
            profilepic.setImageResource(R.drawable.ic_baseline_person_24);
        }
    }
    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }




}