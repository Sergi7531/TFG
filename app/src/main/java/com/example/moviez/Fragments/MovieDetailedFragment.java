package com.example.moviez.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Activities.AppViewModel;
import com.example.moviez.Adapters.CommentAdapter;
import com.example.moviez.Adapters.FilmAdapter;
import com.example.moviez.IMDB;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.example.moviez.Responses;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailedFragment extends AppFragment {

    private static int filmId = 0;
    public ImageView movieImage;
    public ImageView movieBackground;
    public TextView infoMovie,
            movieTitle,
            movieDuration,
            movieRelease,
            movieDirector,
            movieCasting,
            globalUsersRating,
            comentariosTextDetail;
    public RatingBar ratingBar;
    public Button addCommentMovie;
    public static CardView favoriteFloatingButton;
    public static RecyclerView similarFilmsRecyclerView;

    public Button buyButton;
    public ImageView heartImage;

    private Spinner spinner;
    private List<Responses.CastResult> actorItems = new ArrayList<>();
    public static RecyclerView commentsFragmentMovieDetail;
    private final List<Models.Comment> comments = new ArrayList<>();

    public CardView similarCard;

    Models.Film film;


    List<String> status = new ArrayList<>();


    public MovieDetailedFragment() {
    }

    public MovieDetailedFragment(int param1) {
        filmId = param1;
    }

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
        return inflater.inflate(R.layout.fragment_movie_detailed, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);

        AppViewModel.getMovieDetails(filmId);

        AppViewModel.movieDetails.observe(getViewLifecycleOwner(), movie -> {

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


            if(minutes != 0 && hours != 0) {
                movieDuration.setText(hours + "h " + minutes + "m");
            }

            String date = movie.release_date;
            String[] parts = date.split("-");
            if (parts.length == 3) {
                movieRelease.setText(parts[2] + "-" + parts[1] + "-" + parts[0]);
            } else {
                movieRelease.setText(R.string.FechaNoDisponible);
            }

            AppViewModel.getMovieCast(filmId);

            infoMovie.setText(movie.overview);

            actorItems.clear();

            AppViewModel.fullCast.observe(getViewLifecycleOwner(), this::loadActors);

            db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("favoritedFilms").document(String.valueOf(filmId)).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        heartImage.setImageResource(R.drawable.heart);
                    } else {
                        heartImage.setImageResource(R.drawable.heart_empty);
                    }
                }
            });
            favoriteFloatingButton.setOnClickListener(v -> db.collection("users").document(auth.getCurrentUser().getUid()).collection("favoritedFilms").document(String.valueOf(filmId)).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        heartImage.setImageResource(R.drawable.heart_empty);
                        db.collection("users").document(auth.getCurrentUser().getUid()).collection("favoritedFilms").document(String.valueOf(filmId)).delete();
                    } else {
                        heartImage.setImageResource(R.drawable.heart);
                        db.collection("users").document(auth.getCurrentUser().getUid()).collection("favoritedFilms").document(String.valueOf(filmId)).set(movie);
                    }
                }
            }));

            IMDB.api.getRecommendations(movie.id, IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.SearchResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<Responses.SearchResponse> call, @NonNull Response<Responses.SearchResponse> response) {
                    if (response.body() != null) {
                        if (response.body().results.size() > 10) {
                            response.body().results.subList(0, 10);
                        }
                        AppViewModel.similarMovies.postValue(response.body());
                    }
                }

                @Override
                public void onFailure (@NonNull Call < Responses.SearchResponse > call, @NonNull Throwable t) {
                    t.getMessage();
                }
            });

            loadSimilarMovies(AppViewModel.similarMovies.getValue());

            AppViewModel.similarMovies.observe(getViewLifecycleOwner(), this::loadSimilarMovies);

            status.add(0, "Estado");

            if(!LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) ,Integer.parseInt(parts[2])).isAfter(LocalDate.now())) {
                status.add(1, "Vista");
            }

            status.add("Ver más tarde");

            addCommentMovie.setOnClickListener(v -> {
                NewCommentFragment newCommentFragment = new NewCommentFragment(filmId);
                setFragment(newCommentFragment);
            });

            getCommentsFromFirebase(filmId);

            IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<Responses.BillboardResponse> call, @NonNull Response<Responses.BillboardResponse> response) {
                    if (response.body() != null) {
                        for(Models.Film movie : response.body().results) {
                            if (movie.id == filmId) {
                                buyButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Responses.BillboardResponse> call, @NonNull Throwable t) {
                    t.getMessage();
                }
            });



            buyButton.setOnClickListener(v -> {
                AppViewModel.currentFilmId = filmId;
                BuyTicketFragment buyTicketFragment = new BuyTicketFragment(filmId, R.id.frame_detail);
                setFragment(buyTicketFragment);
            });

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    status);
            spinner.setAdapter(dataAdapter);
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
                if (selected.equals("Ver más tarde")) {

                    IMDB.api.getMovie(filmId, IMDB.apiKey, "es-ES").enqueue(new Callback<Models.Film>() {
                        @Override
                        public void onResponse(@NonNull Call<Models.Film> call, @NonNull Response<Models.Film> response) {
                            if (response.body() != null) {
                                film = new Models.Film(response.body().id, response.body().title, response.body().poster_path);
                                addToWatched("moviesToWatch", "watchedFilms");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Models.Film> call, @NonNull Throwable t) {

                        }
                    });

                } else if (selected.equals("Vista")) {
                    IMDB.api.getMovie(filmId, IMDB.apiKey, "es-ES").enqueue(new Callback<Models.Film>() {
                        @Override
                        public void onResponse(@NonNull Call<Models.Film> call, @NonNull Response<Models.Film> response) {
                            if (response.body() != null) {
                                film = new Models.Film(response.body().id, response.body().title, response.body().poster_path);
                                addToWatched("watchedFilms", "moviesToWatch");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Models.Film> call, @NonNull Throwable t) {

                        }
                    });
                } else {
                    removeFromWatchedAndToWatch();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "No has seleccionado nada", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void removeFromWatchedAndToWatch() {
        db.collection("users")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection("moviesToWatch")
                .document(String.valueOf(filmId))
                .delete();

        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("watchedFilms")
                .document(String.valueOf(filmId))
                .delete();
    }

    private void addToWatched(String watchedFilms, String moviesToWatch) {
        db.collection("users")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .collection(watchedFilms)
                .document(String.valueOf(filmId))
                .set(film);

        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection(moviesToWatch)
                .document(String.valueOf(filmId))
                .delete();
    }

    private void setFragment(Fragment fragment) {
        buyButton.setVisibility(View.GONE);
        MovieDetailedFragment.this.getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_detail, fragment)
                .addToBackStack(MovieDetailedFragment.class.getSimpleName())
                .commit();
    }

    private void getCommentsFromFirebase(int filmId) {
        db.collection("comments").document(String.valueOf(filmId)).collection("comments").get().addOnCompleteListener(task -> {
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
                    comentariosTextDetail.setText(R.string.Comentarios);
                } else {
                    globalUsersRating.setText(" - ");
                    commentsFragmentMovieDetail.setVisibility(View.GONE);
                    comentariosTextDetail.setText(R.string.NoHayComentarios);
                }

            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void loadActors(Responses.FullCastResponse cast) {
        actorItems = Objects.requireNonNull(AppViewModel.fullCast.getValue()).cast;

        if (actorItems.size() > 4) {
            actorItems = actorItems.subList(0, 4);
        }

        List<Responses.CrewResult> crewItems = AppViewModel.fullCast.getValue().crew;

        for (int i = 0; i < crewItems.size(); i++) {
            if (crewItems.get(i).job.equals("Director")) {
                movieDirector.setText(crewItems.get(i).name);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < actorItems.size(); i++) {
            if (i == actorItems.size()-1) {
                sb.append(cast.cast.get(i).name).append("\n");
            } else {
                sb.append(cast.cast.get(i).name).append(",\n");
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
        favoriteFloatingButton = view.findViewById(R.id.favoriteFloatingButton);
        similarFilmsRecyclerView = view.findViewById(R.id.similarFilmsRecyclerView);
        buyButton = view.findViewById(R.id.goTicketButton);
        spinner = view.findViewById(R.id.spinner);
        heartImage = view.findViewById(R.id.heartImage);
        infoMovie = view.findViewById(R.id.infoPeli);
        similarCard = view.findViewById(R.id.similarCard);
    }

    private void loadSimilarMovies(Responses.SearchResponse similarMovies) {
        if(similarMovies != null) {
            if (similarMovies.results.size() != 0) {
                similarCard.setVisibility(View.VISIBLE);
                similarFilmsRecyclerView.setAdapter(new FilmAdapter(similarMovies.results, requireContext(), MovieDetailedFragment.this));
                similarFilmsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
            } else {
                similarCard.setVisibility(View.GONE);
            }
        } else {
            similarCard.setVisibility(View.GONE);
        }
    }
}