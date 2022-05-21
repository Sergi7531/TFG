package com.example.moviez;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.LocalTime;
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
            public void onResponse(@NonNull Call<Responses.BillboardResponse> call, @NonNull Response<Responses.BillboardResponse> response) {
                Responses.BillboardResponse responseBody = response.body();
                if (responseBody != null) {
                    Responses.Dates dates = responseBody.dates;
                    dateStart.postValue(dates.minimum);

                    LocalDate localDate = LocalDate.parse(dates.maximum);
                    localDate.plusDays(daysUntilDateEnd);
                    daysUntilDateEnd = 15;
                    dateEnd.postValue(LocalDate.now().plusDays(daysUntilDateEnd).toString());
                }
                getCinemas();
                createMovieSessions(daysUntilDateEnd);
            }

            @Override
            public void onFailure(@NonNull Call<Responses.BillboardResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createMovieSessions(int daysUntilDateEnd) {

        IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {

            @Override
            public void onResponse(@NonNull Call<Responses.BillboardResponse> call, @NonNull Response<Responses.BillboardResponse> response) {
                if (response.body() != null) {
                    Responses.BillboardResponse responseBody = response.body();
                    for (Models.Film film : responseBody.results) {

                        IMDB.api.getMovie(film.id, IMDB.apiKey, "es-ES").enqueue(new Callback<Models.Film>() {
                            @Override
                            public void onResponse(@NonNull Call<Models.Film> call, @NonNull Response<Models.Film> response) {
                                if (response.body() != null) {
                                    FirebaseFirestore.getInstance().collection("movie_sessions").document(String.valueOf(film.id)).set(new Models.Film(film.id, response.body().poster_path, response.body().runtime, response.body().overview, response.body().title)).addOnSuccessListener(success -> cinemas.observeForever(cinemas -> {
                                        Models.Cinema cinema = cinemas.get(new Random().nextInt(cinemas.size()));

                                        FirebaseFirestore.getInstance().collection("movie_sessions").document(String.valueOf(film.id)).collection("cinemas").get().addOnCompleteListener(query -> {
                                            if (query.isSuccessful()) {
                                                if (query.getResult().size() == 0) {
                                                    FirebaseFirestore.getInstance().collection("movie_sessions").document(String.valueOf(film.id)).collection("cinemas").document(cinema.cinemaid).set(cinema);

                                                    int roomId = rooms.get(new Random().nextInt(rooms.size()));
                                                    FirebaseFirestore.getInstance().collection("movie_sessions")
                                                            .document(String.valueOf(film.id)).collection("cinemas")
                                                            .document(cinema.cinemaid).collection("rooms")
                                                            .document(String.valueOf(roomId))
                                                            .set(new Models.Room(roomId, "Sala " + roomId, cinema.cinemaid, film.id));
                                                    if (rooms.stream().anyMatch(room -> room == roomId)) {
                                                        rooms.remove((Integer) roomId);
                                                    }

                                                    for (int i = 0; i <= daysUntilDateEnd; i++) {
                                                        LocalDate localDate = LocalDate.now().plusDays(i);
                                                        for (int j = 0; j < 24; j++) {
                                                            LocalTime localTime = LocalTime.of(j, 0);

                                                            if (localTime.isAfter(LocalTime.of(10, 0)) && localTime.isBefore(LocalTime.of(22, 0))) {
                                                                FirebaseFirestore.getInstance().collection("movie_sessions")
                                                                        .document(String.valueOf(film.id))
                                                                        .collection("cinemas")
                                                                        .document(cinema.cinemaid)
                                                                        .collection("rooms")
                                                                        .document(String.valueOf(roomId))
                                                                        .collection("sessions")
                                                                        .document(localDate.getMonthValue() + "-" + localDate.getDayOfMonth() + "-" + localTime.getHour())
                                                                        .set(new Models.Session(localDate.getMonthValue() + "-" + localDate.getDayOfMonth() + "-" + localTime.getHour(), localDate.getMonthValue() + "", localDate.getDayOfMonth() + "", localTime.getHour() + ""));
                                                            }
                                                            j = j + 2;
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    }));

                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Models.Film> call, @NonNull Throwable t) {
                                Log.d("Error", t.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Responses.BillboardResponse> call, @NonNull Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }
}
