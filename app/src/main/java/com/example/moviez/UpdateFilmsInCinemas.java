package com.example.moviez;

import android.os.Build;

import androidx.annotation.RequiresApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFilmsInCinemas {

    public static void initDates() {

        IMDB.api.getNowPlaying(IMDB.apiKey, "es-ES", 1).enqueue(new Callback<Responses.BillboardResponse>() {

            @Override
            public void onResponse(Call<Responses.BillboardResponse> call, Response<Responses.BillboardResponse> response) {

            }

            @Override
            public void onFailure(Call<Responses.BillboardResponse> call, Throwable t) {

            }


        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void updateFilmsInCinemas() {

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






    }

}
