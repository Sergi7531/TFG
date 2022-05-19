package com.example.moviez;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class IMDB {

    public static String apiKey = "508cb04cd96b9cba0b2e09d5952ac3f5";

    public static String BASE_URL = "https://api.themoviedb.org/3/";

    public static Api api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
//            .client(new OkHttpClient.Builder()
                    // log interceptor
//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        @NonNull
//                        public Response intercept(@NonNull Chain chain) throws IOException {
//                            Request request = chain.request();
//                            Log.e("INTERCEPTOR", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
//
//                            okhttp3.Response response = chain.proceed(request);
//                            Log.e("INTERCEPTOR---", String.format("Received response for %s %n%s", response.request().url(), response.headers()));
//
//                            return response;
//                        }
//                    })
//                    .build()
//            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);

    public interface Api {

        @GET("movie/{movie_id}/credits")
        Call<Responses.FullCastResponse> getCast(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language);

        @GET("movie/{movie_id}")
        Call<Models.Film> getMovie(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language);

        @GET("movie/{movie_id}/recommendations")
        Call<Responses.SearchResponse> getRecommendations(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

        @GET("movie/upcoming")
        Call<Responses.BillboardResponse> getUpcoming(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

        @GET("movie/now_playing")
        Call<Responses.BillboardResponse> getNowPlaying(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

        @GET("movie/top_rated")
        Call<Responses.SearchResponse> getMoviesTopRated(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

        @GET("search/movie")
        Call<Responses.SearchResponse> search(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query, @Query("page") int page);

        @GET("movie/{movie_id}/videos")
        Call<Responses.Videos> getVideos(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);
    }
}
