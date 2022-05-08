package com.example.moviez;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static int filmId = 0;
    public static ImageView movieImage;
    public static ImageView movieBackground;
    public static TextView movieTitle;
    public static TextView movieDuration;
    public static TextView movieRelease;
    public static TextView movieDirector;
    public static TextView movieCasting;
    public static TextView globalUsersRating;
    public static TextView comentariosTextDetail;
    public static RatingBar ratingBar;
    public static Button addCommentMovie;

//    Intent to BuyTicketsFragment:
    public static Button buyButton;

    private List<Responses.CastResult> actorItems = new ArrayList<>();
    private List<Responses.CrewResult> crewItems = new ArrayList<>();
    public static RecyclerView commentsFragmentMovieDetail;
    private List<Models.Comment> comments = new ArrayList<>();


    public MovieDetailedFragment() {
        // Required empty public constructor
    }

    public MovieDetailedFragment(int param1) {
        filmId = param1;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MovieDetailed.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailedFragment newInstance(String param1) {
        MovieDetailedFragment fragment = new MovieDetailedFragment();
        Bundle args = new Bundle();
        args.putInt(param1, filmId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filmId = getArguments().getInt("filmId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_detailed, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);

        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);


        viewModel.getMovieDetails(filmId);

        viewModel.movieDetails.observe(getViewLifecycleOwner(), movie -> {

            Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/original" + movie.poster_path)
                    .centerCrop()
                    .into(movieImage);

            Glide.with(requireContext())
                    .load("https://image.tmdb.org/t/p/original" + movie.backdrop_path)
                    .centerCrop()
                    .into(movieBackground);

            movieTitle.setText(movie.title);

            int hours = movie.runtime / 60;
            int minutes = movie.runtime % 60;

            movieDuration.setText(hours + "h " + minutes + "m");

//            Convert date in string (format YYYY-MM-DD) to DD-MM-YYYY:
            String date = movie.release_date;
            String[] parts = date.split("-");
            movieRelease.setText(parts[2] + "-" + parts[1] + "-" + parts[0]);


            viewModel.getMovieCast(filmId);

            actorItems.clear();

            viewModel.fullCast.observe(getViewLifecycleOwner(), cast -> { ;
                loadActors(viewModel, cast);
            });
        });

        addCommentMovie.setOnClickListener(v -> {
//            setFragment(new AddCommentFragment(filmId));
        });

        getCommentsFromFirebase(filmId);
    }

    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_home, fragment)
                .commit();
    }

    private void getCommentsFromFirebase(int filmId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("films").document(String.valueOf(filmId)).collection("comments").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Models.Comment comment = document.toObject(Models.Comment.class);
                    comments.add(comment);
                }
                commentsFragmentMovieDetail.setLayoutManager(new LinearLayoutManager(getContext()));
                commentsFragmentMovieDetail.setAdapter(new CommentAdapter(comments, requireContext()));

                double averageRating = 0;
                for(Models.Comment comment : comments) {
                    averageRating += comment.rating;
                }

                if(comments.size() > 0) {
                    averageRating = averageRating / comments.size();
                    globalUsersRating.setText(String.format("%.1f", averageRating));
                    ratingBar.setRating((float) averageRating);
                    commentsFragmentMovieDetail.setVisibility(View.VISIBLE);
                    comentariosTextDetail.setText("Comentarios");
                } else {
                    globalUsersRating.setText(" - ");
                    commentsFragmentMovieDetail.setVisibility(View.GONE);
                    comentariosTextDetail.setText("No hay comentarios");
                }


            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void loadActors(AppViewModel viewModel, Responses.FullCastResponse cast) {
        actorItems = viewModel.fullCast.getValue().cast;

        if (actorItems.size() > 4) {
            actorItems = actorItems.subList(0, 4);
        }


        crewItems = viewModel.fullCast.getValue().crew;

//        Get the director:
        for (int i = 0; i < crewItems.size(); i++) {
            if (crewItems.get(i).job.equals("Director")) {
                movieDirector.setText(crewItems.get(i).name);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < actorItems.size(); i++) {
            if (i == actorItems.size()-1) {
                sb.append(cast.cast.get(i).name + "\n");
            } else {
                sb.append(cast.cast.get(i).name + ",\n");
            }
        }
        movieCasting.setText(sb.toString().trim());
    }

    private void hook(View view) {
        movieImage = view.findViewById(R.id.movieImage);
        movieBackground = view.findViewById(R.id.movieBackground);
        movieTitle = view.findViewById(R.id.movieTitle);
        movieDuration = view.findViewById(R.id.movieDuration);
        movieRelease = view.findViewById(R.id.movieRelease);
        movieDirector = view.findViewById(R.id.movieDirector);
        movieCasting = view.findViewById(R.id.movieCasting);
        globalUsersRating = view.findViewById(R.id.globalUsersRating);
        commentsFragmentMovieDetail = view.findViewById(R.id.commentsFragmentMovieDetail);
        comentariosTextDetail = view.findViewById(R.id.comentariosTextDetail);
        ratingBar = view.findViewById(R.id.ratingBar);
        addCommentMovie = view.findViewById(R.id.addCommentMovie);
    }
}