package com.example.moviez.Activities;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviez.IMDB;
import com.example.moviez.Models;
import com.example.moviez.Responses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppViewModel extends ViewModel {

    public static MutableLiveData<Responses.BillboardResponse> upcomingMoviesResponse = new MutableLiveData<>();
    public static MutableLiveData<Responses.BillboardResponse> actualMoviesInCinemaResponse = new MutableLiveData<>();
    public static MutableLiveData<Responses.SearchResponse> moviesByQuery = new MutableLiveData<>();
    public static MutableLiveData<Responses.SearchResponse> forYouMovies = new MutableLiveData<>();
    public static MutableLiveData<Responses.SearchResponse> similarMovies = new MutableLiveData<>();
    public static MutableLiveData<Models.Film> movieDetails = new MutableLiveData<>();
    public static MutableLiveData<Responses.FullCastResponse> fullCast = new MutableLiveData<>();
    public static MutableLiveData<String> maximumDate = new MutableLiveData<>();
    public MutableLiveData<Uri> uriImagenSeleccionada = new MutableLiveData<>();

    public static int contPage = 0;
    public static int currentFilmId;

    public Models.User userlogged;

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void getUpcomingMovies () {
        IMDB.api.getUpcoming(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {
            @Override
            public void onResponse(@NonNull Call<Responses.BillboardResponse> call, @NonNull Response<Responses.BillboardResponse> response) {
                upcomingMoviesResponse.postValue(response.body());
            }
            @Override
            public void onFailure(@NonNull Call<Responses.BillboardResponse> call, @NonNull Throwable t) {
                t.getMessage();
            }
        });
    }

    public static void getActualCinemaMovies () {
        IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {
            @Override
            public void onResponse(@NonNull Call<Responses.BillboardResponse> call, @NonNull Response<Responses.BillboardResponse> response) {
                actualMoviesInCinemaResponse.postValue(response.body());
                if (response.body() != null) {
                    maximumDate.postValue(response.body().dates.maximum);
                }
            }
            @Override
            public void onFailure (@NonNull Call<Responses.BillboardResponse> call, @NonNull Throwable t) {
                t.getMessage();
            }
        });
    }

    public static void searchMoviesByQuery (String query) {
        IMDB.api.search(IMDB.apiKey, "es-ES", query, 1).enqueue(new Callback<Responses.SearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<Responses.SearchResponse> call, @NonNull Response<Responses.SearchResponse> response) {
                if (response.body() != null) {
                    Responses.SearchResponse searchResponse = response.body();
                    if (searchResponse.results.size() > 10) {
                        searchResponse.results.subList(10, searchResponse.results.size()).clear();
                        List<Models.Film> moviesToShowSearch = searchResponse.results;
                        moviesToShowSearch.add(new Models.Film( "Más resultados...", "null"));

                        response.body().results = moviesToShowSearch;
                        moviesByQuery.postValue(response.body());
                    }
                }
            }
            @Override
            public void onFailure (@NonNull Call<Responses.SearchResponse> call, @NonNull Throwable t) {
                t.getMessage();
            }
        });
    }

    public static void getMoviesForYou (List<Integer> genresUser) {

        forYouMovies.setValue(null);

        contPage = (int) (Math.random() * 2) + 1;

        getMoviesForYouByCollection("favoritedFilms");

        getMoviesForYouByCollection("watchedFilms");

        getMoviesForYouByCollection("moviesToWatch");


        if(forYouMovies.getValue() == null) {
            getMoviesByGenres(genresUser);
        }

    }

    private static void getMoviesByGenres(List<Integer> genresUser) {
//        Get recommendations by genres:
        IMDB.api.getMoviesTopRated(IMDB.apiKey, "es-ES", contPage).enqueue(new Callback<Responses.SearchResponse>() {
            @Override
            public void onResponse(Call<Responses.SearchResponse> call, Response<Responses.SearchResponse> response) {
                if (response.body() != null) {
                    Responses.SearchResponse moviesForYouResponse = response.body();
                    LinkedHashSet<Models.Film> filmsToShow = new LinkedHashSet<>();
//                    For each film, we need to check if any of the user's favorite genres is in the film's genres:
                    for (Models.Film movie : moviesForYouResponse.results) {
                        for (Integer genre : genresUser) {
                            if (movie.genre_ids.contains(genre)) {
                                filmsToShow.add(movie);
                            }
                        }
                    }
                    moviesForYouResponse.results.clear();
                    moviesForYouResponse.results.addAll(filmsToShow);
                    forYouMovies.postValue(moviesForYouResponse);
                }
            }

            @Override
            public void onFailure(Call<Responses.SearchResponse> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private static void getMoviesForYouByCollection (String collection) {

        db.collection("users").document(auth.getCurrentUser().getUid()).collection(collection).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IMDB.api.getRecommendations(Integer.parseInt(documentSnapshot.getId()), IMDB.apiKey, "es-ES", contPage).enqueue(new Callback<Responses.SearchResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse (@NonNull Call<Responses.SearchResponse> call, @NonNull Response<Responses.SearchResponse> response) {
                        if (response.body() != null) {
                            List<Models.Film> moviesToShow = response.body().results;
                            if (moviesToShow.size() > 5) {
                                moviesToShow = moviesToShow.subList(0, 5);
                            }
                            Responses.SearchResponse searchResponses = new Responses.SearchResponse();
                            searchResponses.results = moviesToShow;
                            if(forYouMovies.getValue() != null) {
                                searchResponses.results.addAll(forYouMovies.getValue().results);
                            }
                            forYouMovies.postValue(searchResponses);
                        }
                    }
                    @Override
                    public void onFailure (@NonNull Call<Responses.SearchResponse> call, @NonNull Throwable t) {
                        t.getMessage();
                    }
                });
            }
        });
    }

    public static void getMovieDetails (int filmId) {
        movieDetails = new MutableLiveData<>();
        IMDB.api.getMovie(filmId, IMDB.apiKey, "es-ES").enqueue(new Callback<Models.Film>() {
            @Override
            public void onResponse (@NonNull Call<Models.Film> call, @NonNull Response<Models.Film> response) {
                if (response.body() != null) {
                    movieDetails.postValue(response.body());
                }
            }
            @Override
            public void onFailure (@NonNull Call<Models.Film> call, @NonNull Throwable t) {
                t.getMessage();
            }
        });
    }


    public static void getMovieCast (int filmId) {
        fullCast = new MutableLiveData<>();
        IMDB.api.getCast(filmId, IMDB.apiKey, "es-ES").enqueue(new Callback<Responses.FullCastResponse>() {
            @Override
            public void onResponse (@NonNull Call<Responses.FullCastResponse> call, @NonNull Response<Responses.FullCastResponse> response) {
                if (response.body() != null) {
                    fullCast.postValue(response.body());
                }
            }
            @Override
            public void onFailure (@NonNull Call<Responses.FullCastResponse> call, @NonNull Throwable t) {
                t.getMessage();
            }
        });
    }

    public void setUriImagenSeleccionada (Uri uri) {
        uriImagenSeleccionada.setValue(uri);
    }

}
