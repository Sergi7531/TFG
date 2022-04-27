package com.example.moviez;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppViewModel extends ViewModel {
    static MutableLiveData<Responses.BillboardResponse> upcomingMoviesResponse = new MutableLiveData<>();

    static MutableLiveData<Responses.BillboardResponse> actualMoviesInCinemaResponse = new MutableLiveData<>();

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
}
