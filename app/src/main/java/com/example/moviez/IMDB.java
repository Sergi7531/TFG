package com.example.moviez;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class IMDB {

    public static String apiKey = "508cb04cd96b9cba0b2e09d5952ac3f5";

    public static String BASE_URL = "https://api.themoviedb.org/3";

    public static Api api = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);

    public interface Api {

        @GET("/movie/{movie_id}/credits?api_key={api_key}&language={language}")
        Call<Responses.Cast> getCast(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language);

        @GET("/movie/{movie_id}?api_key={api_key}&language={language}")
        Call<Responses.MovieResponse> getMovie(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language);

        @GET("/movie/{movie_id}/similar?api_key={api_key}&language={language}&page={page}")
        Call<Responses.SimilarResponse> getSimilar(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

        @GET("/movie/now_playing?api_key={api_key}&language={language}&page={page}")
        Call<Responses.NowPlayingResponse> getNowPlaying(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

        @GET("/movie/{movie_id}/recommendations?api_key={api_key}&language={language}&page={page}")
        Call<Responses.RecommendationResponse> getRecommendations(@Path("movie_id") int movie_id, @Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

        @GET("/movie/upcoming?{api_key}&language={language}&page={page}")
        Call<Responses.UpcomingResponse> getUpcoming(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);

//        Check this one first and then use bottom one to get ALL countries and match the iso_3166_1 to every String in this endpoint's' response.
        @GET("/configuration/primary_translations?api_key={api_key}")
        Call<String[]> getTranslations(@Query("api_key") String apiKey);

        @GET("/configuration/countries?api_key={api_key}")
        Call<Responses.CountryResponse> getCountries(@Query("api_key") String apiKey);

        @GET("/search/movie?api_key={api_key}&language={language}&query={query}&page={page}")
        Call<Responses.SearchResponse> search(@Query("api_key") String apiKey, @Query("language") String language, @Query("query") String query, @Query("page") int page);
    }


}
