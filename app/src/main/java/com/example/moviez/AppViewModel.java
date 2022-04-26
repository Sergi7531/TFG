package com.example.moviez;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppViewModel extends ViewModel {
    static MutableLiveData<Responses.UpcomingResponse> upcomingMoviesResponse = new MutableLiveData<>();


    static void getUpcomingMovies() {
       IMDB.api.getUpcoming(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.UpcomingResponse>() {
           @Override
           public void onResponse(Call<Responses.UpcomingResponse> call, Response<Responses.UpcomingResponse> response) {
               upcomingMoviesResponse.postValue(response.body());
           }

           @Override
           public void onFailure(Call<Responses.UpcomingResponse> call, Throwable t) {
               t.getMessage();
//               Da error?
//               Toast.makeText(AppViewModel.this, "Error al obtener las peliculas", Toast.LENGTH_SHORT).show();
           }
       });
    }
}
