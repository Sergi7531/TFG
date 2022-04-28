package com.example.moviez;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppViewModel extends ViewModel {
    static MutableLiveData<Responses.BillboardResponse> upcomingMoviesResponse = new MutableLiveData<>();
    static MutableLiveData<Responses.BillboardResponse> actualMoviesInCinemaResponse = new MutableLiveData<>();
    static MutableLiveData<Responses.SearchResponse> moviesByQuery = new MutableLiveData<>();

//    MOVIESFRAGMENT:

    public static void getUpcomingMovies() {
       IMDB.api.getUpcoming(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {
           @Override
           public void onResponse(Call<Responses.BillboardResponse> call, Response<Responses.BillboardResponse> response) {
               upcomingMoviesResponse.postValue(response.body());
           }
           @Override
           public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {
               t.getMessage();
//               Da error?
//               Toast.makeText(AppViewModel.this, "Error al obtener las peliculas", Toast.LENGTH_SHORT).show();
           }
       });
    }

    public static void getActualCinemaMovies() {
        IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {
            @Override
            public void onResponse(Call<Responses.BillboardResponse> call, Response<Responses.BillboardResponse> response) {
                actualMoviesInCinemaResponse.postValue(response.body());
            }
            @Override
            public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {
                t.getMessage();
                //                Da error?
                //                Toast.makeText(AppViewModel.this, "Error al obtener las peliculas", Toast.LENGTH_SHORT).show();
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
                        List<Models.Movie> moviesToShowSearch = searchResponse.results;
                        moviesToShowSearch.add(new Models.Movie("Más resultados...", "null"));

                        response.body().results = moviesToShowSearch;
                        moviesByQuery.postValue(response.body());
                    }
                }
            }
            @Override
            public void onFailure(Call<Responses.SearchResponse> call, Throwable t) {
                t.getMessage();
                //                Da error?
                //                Toast.makeText(AppViewModel.this, "Error al obtener las peliculas", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
