package com.example.moviez;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFilmsInCinemas {

    public static MutableLiveData<String> dateStart = new MutableLiveData<>();
    public static MutableLiveData<String> dateEnd = new MutableLiveData<>();

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    //    Num of days until the dateEnd
    public static int daysUntilDateEnd = 0;

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

//                createMovieSessions(daysUntilDateEnd);
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

                while(daysUntilDateEnd > 0) {

                }
            }

            @Override
            public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {

            }

//        In the cinemas collection, get all the cinemas. For each cinema, create a new collection with every day from the current date to the billboard_finish date:
//
//        db.collection("cinemas").get().addOnSuccessListener(collectionSnapshot -> {
//            for (DocumentSnapshot documentSnapshot : collectionSnapshot.getDocuments()) {
//
////                Get number of days between today and the billboard_finish date:
//
//                int days = 0;
//                String[] billboard_finish_date_split = billboard_finish_date.split("-");
//                String[] current_date_split = current_date.split("-");
//
//                int billboard_finish_date_year = Integer.parseInt(billboard_finish_date_split[0]);
//
//
//
//                //                db.collection("cinemas")
//
//            }
//        });

        });

    }

}
