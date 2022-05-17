package com.example.moviez.Activities;

import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.moviez.IMDB;
import com.example.moviez.Models;
import com.example.moviez.Responses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
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
    //    We will use this counters in case we need to use the "page" param (so we take control of the results number)
    public static int contPage = 0;

    public MutableLiveData<Uri> uriImagenSeleccionada = new MutableLiveData<>();

    public Models.User userlogged;

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth auth = FirebaseAuth.getInstance();

    public static int currentFilmId;



    public static void getUpcomingMovies() {
        IMDB.api.getUpcoming(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {
            @Override
            public void onResponse(Call<Responses.BillboardResponse> call, Response<Responses.BillboardResponse> response) {
                upcomingMoviesResponse.postValue(response.body());
            }
            @Override
            public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    public static void getActualCinemaMovies() {
        IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {
            @Override
            public void onResponse(Call<Responses.BillboardResponse> call, Response<Responses.BillboardResponse> response) {
                actualMoviesInCinemaResponse.postValue(response.body());
                maximumDate.postValue(response.body().dates.maximum);
            }
            @Override
            public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {
                t.getMessage();
            }
        });
    }

//    This query will be executed a lot of times, so we need to cache the results by applying a "internPages" variable that will limit the results to 10 at each API request:

    public static void searchMoviesByQuery(String query) {
        IMDB.api.search(IMDB.apiKey, "es-ES", query, 1).enqueue(new Callback<Responses.SearchResponse>() {
            @Override
            public void onResponse(Call<Responses.SearchResponse> call, Response<Responses.SearchResponse> response) {
//                We need to limit the number of results to 10:
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
            public void onFailure(Call<Responses.SearchResponse> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    public static void getMoviesForYou(List<Integer> genresUser) {

        contPage = (int) (Math.random() * 3) + 1;

        getMoviesForYouByCollection("favoritedFilms");

        getMoviesForYouByCollection("lastViewedFilms");

        getMoviesForYouByCollection("moviesToWatch");

//        If after the 3 methods the forYouMovies list is empty,
//        We need to get the movies from the API and filter them by the genres the user has selected:

        if (forYouMovies.getValue() == null) {
//            Get the api response:
            IMDB.api.getMoviesTopRated(IMDB.apiKey, "es-ES", contPage).enqueue(new Callback<Responses.SearchResponse>() {
                @Override
                public void onResponse(Call<Responses.SearchResponse> call, Response<Responses.SearchResponse> response) {
//                   Iterate the user genres and get the movies that match with them:
                    List<Models.Film> moviesByGenres = new ArrayList<>();
                    if (genresUser.size() > 0) {
                        for (int genre : genresUser) {
//                           For with i = 0, we will get the movies that match with the first genre of the user:
                            for (int i = 0; i < response.body().results.size(); i++) {
                                if (response.body().results.get(i).genre_ids.contains(genre)) {
                                    moviesByGenres.add(response.body().results.get(i));
                                }
                            }
                            response.body().results = moviesByGenres;
                            forYouMovies.postValue(response.body());
                        }
                    } else {
                        forYouMovies.postValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Responses.SearchResponse> call, Throwable t) {
                    t.getMessage();
                }
            });
        }


    }

    private static void getMoviesForYouByCollection(String favoritedFilms) {
        db.collection("users").document(auth.getCurrentUser().getUid()).collection(favoritedFilms).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                IMDB.api.getRecommendations(Integer.parseInt(documentSnapshot.getId()), IMDB.apiKey, "es-ES", contPage).enqueue(new Callback<Responses.SearchResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<Responses.SearchResponse> call, Response<Responses.SearchResponse> response) {
                        if (response.body() != null) {
                            if (response.body().results.size() > 5) {
                                response.body().results.subList(0, 5);
                            }
                            forYouMovies.postValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Responses.SearchResponse> call, Throwable t) {
                        t.getMessage();
                    }
                });

            }
        });
    }

    public static void moviesForYouByGenres() {
        forYouMovies.observeForever(movies -> {
            System.out.println(movies.results.toString());
            if(movies == null) {

                IMDB.api.getMoviesTopRated(IMDB.apiKey, "es-ES", contPage).enqueue(new Callback<Responses.SearchResponse>() {
                    @Override
                    public void onResponse(Call<Responses.SearchResponse> call, Response<Responses.SearchResponse> response) {
                        if (response.body() != null) {
                            if (response.body().results.size() > 6) {
                                for (Models.Film film : response.body().results) {
                                    db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnSuccessListener(success -> {
                                        for (int genre_id : film.genre_ids) {
                                            for (int genre : success.toObject(Models.User.class).favoriteGenres) {
                                                if (genre_id == genre) {
                                                    forYouMovies.postValue(response.body());
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Responses.SearchResponse> call, Throwable t) {
                        t.getMessage();
                    }
                });
            }
        });
    }



    //    Create a method to get the movie's details:
    public static void getMovieDetails(int filmId) {
        movieDetails = new MutableLiveData<>();
        IMDB.api.getMovie(filmId, IMDB.apiKey, "es-ES").enqueue(new Callback<Models.Film>() {
            @Override
            public void onResponse(Call<Models.Film> call, Response<Models.Film> response) {
                if (response.body() != null) {
                    movieDetails.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<Models.Film> call, Throwable t) {
                t.getMessage();
            }
        });
    }


    //    CReate a method to get the movie's cast:
    public static void getMovieCast(int filmId) {
        fullCast = new MutableLiveData<>();
        IMDB.api.getCast(filmId, IMDB.apiKey, "es-ES").enqueue(new Callback<Responses.FullCastResponse>() {
            @Override
            public void onResponse(Call<Responses.FullCastResponse> call, Response<Responses.FullCastResponse> response) {
                if (response.body() != null) {
                    fullCast.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Responses.FullCastResponse> call, Throwable t) {
                t.getMessage();
            }
        });

    }


    public void setUriImagenSeleccionada(Uri uri) {
        uriImagenSeleccionada.setValue(uri);
    }

}
