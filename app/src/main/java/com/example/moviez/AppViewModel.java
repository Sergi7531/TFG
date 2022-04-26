package com.example.moviez;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppViewModel extends ViewModel {
    MutableLiveData<Responses.UpcomingResponse> tal = new MutableLiveData<>();

    void getUpcomingMovies() throws IOException {
       IMDB.api.getUpcoming(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.UpcomingResponse>() {
           @Override
           public void onResponse(Call<Responses.UpcomingResponse> call, Response<Responses.UpcomingResponse> response) {
                tal.postValue(response.body());
           }

           @Override
           public void onFailure(Call<Responses.UpcomingResponse> call, Throwable t) {

           }
       });
    }
}
