package com.example.moviez;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class IMDB {

    public String apiKey = "508cb04cd96b9cba0b2e09d5952ac3f5";

    public static String BASE_URL = "https://api.themoviedb.org/3";

    public static Api api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);

    public interface Api {
        String apiKey = null;

        default Api(String apiKey) {
            this.apiKey = apiKey;
        }

//        @GET("/movie/{movie_id}/credits")
//        Call<> usoCuota(@Path("movie_id") String texto, @Query("api_key") String api_key);


    }

    void viewmodel() {

    }
}
