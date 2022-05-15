package com.example.moviez;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFilmsInCinemas {

    public static MutableLiveData<String> dateStart = new MutableLiveData<>();
    public static MutableLiveData<String> dateEnd = new MutableLiveData<>();

    public static List<Integer> rooms = new ArrayList<>();

    public static int daysUntilDateEnd = 0;

    public static MutableLiveData<List<Models.Cinema>> cinemas = new MutableLiveData<>();

    public static void getCinemas() {
        FirebaseFirestore.getInstance().collection("cinemas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cinemas.postValue(task.getResult().toObjects(Models.Cinema.class));
                rooms = new ArrayList<>(task.getResult().size()*10);
                for (int i = 0; i < task.getResult().size(); i++) {
                    for (int j = 1; j < 11; j++) {
                        rooms.add(i*10 + j);
                    }
                }
            }
        });
    }

    public static void initDates() {

        IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Responses.BillboardResponse> call, Response<Responses.BillboardResponse> response) {
//                Get the Dates from the response:
                Responses.BillboardResponse responseBody = response.body();
                if (responseBody != null) {
                    Responses.Dates dates = responseBody.dates;
                    dateStart.postValue(dates.minimum);
                    dateEnd.postValue(dates.maximum);

                    //Get the number of days until the dateEnd
                    LocalDate localDate = LocalDate.parse(dates.maximum);
                    LocalDate localDate2 = LocalDate.now();
                    daysUntilDateEnd = localDate.getDayOfYear() - localDate2.getDayOfYear();
                    System.out.println("Days until dateEnd: " + daysUntilDateEnd);
                }
                getCinemas();
                createMovieSessions(daysUntilDateEnd);
            }

            @Override
            public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createMovieSessions(int daysUntilDateEnd) {

        IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {

            @Override
            public void onResponse(Call<Responses.BillboardResponse> call, Response<Responses.BillboardResponse> response) {
                if (response.body() != null) {
                    Responses.BillboardResponse responseBody = response.body();
                    for (Models.Film film : responseBody.results) {
                            FirebaseFirestore.getInstance().collection("movie_sessions").document(String.valueOf(film.id)).set(new Models.Film(film.id, film.title, film.poster_path));


                            cinemas.observeForever(cinemas -> {
                                Models.Cinema cinema = cinemas.get(new Random().nextInt(cinemas.size()));
                                FirebaseFirestore.getInstance().collection("movie_sessions").document(String.valueOf(film.id)).collection("cinemas").document(cinema.cinemaid).set(cinema);

//                                    Add the rooms to the cinema (randomly) from the list of rooms:


                                    int roomId = rooms.get(new Random().nextInt(rooms.size()));
                                    FirebaseFirestore.getInstance().collection("movie_sessions").document(String.valueOf(film.id)).collection("cinemas").document(cinema.cinemaid).collection("rooms").document(String.valueOf(roomId)).set(new Models.Room(roomId, "Sala " + roomId, cinema.cinemaid));
                                    if (rooms.stream().anyMatch(room -> room == roomId)) {
                                        rooms.remove(rooms.indexOf(roomId));
                                    }

//                                for (int j = 0; j < 10; j++) {

                                });



                    }
                }
            }

            @Override
            public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {

            }
        });

    }

}
