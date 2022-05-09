package com.example.moviez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyTicketFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class BuyTicketFragment extends AppFragment {

    private static int filmId = 0;

    private static int roomsInSelectedCinema = 0;
    List<Models.Cinema> allCinemas = new ArrayList<>();
    List<Models.Cinema> cinemasNamesToShow = new ArrayList<>();

    Models.Cinema selectedCinema = new Models.Cinema();



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuyTicketFragment() {
        // Required empty public constructor
    }


    public BuyTicketFragment(int param1) {
        filmId = param1;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyTicketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyTicketFragment newInstance(String param1, String param2) {
        BuyTicketFragment fragment = new BuyTicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buy_ticket, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//      Consulta a firebase (coleccion cinemas):
        db.collection("cinemas").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Models.Cinema cinema = documentSnapshot.toObject(Models.Cinema.class);
                allCinemas.add(cinema);
            }

//            TODO: Finish this part

//            Cuando el cine esté seleccionado en el dropdown, se muestra la lista de salas:
//            spinnerCinemas.setOnItemSelectedListener((adapterView, view1, i, l) -> {
//                Models.Cinema cinema = cinemasNamesToShow.get(i);
//                db.collection("cinemas").document(cinema.cinemaid).collection("rooms").get().addOnSuccessListener(queryDocumentSnapshotsRooms -> {
//                    roomsInSelectedCinema = queryDocumentSnapshotsRooms.getDocuments().size();
//                    selectedCinema = allCinemas.get(i);
//                });
//
//            });
//
//            for(int i = 0; i < allCinemas.size(); i++) {
//                db.collection("cinemas")
//                        .document(cinemasNamesToShow.get().cinemaid)
//                        .collection("rooms")
//                        .document(String.valueOf(i)).collection("films").get()
//                        .addOnSuccessListener(queryDocumentSnapshotsRooms -> {
//
////                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshotsRooms.getDocuments()) {
////                                if (documentSnapshot.getId().equals(String.valueOf(filmId))) {
////
////                                }
////                            }
//
//                        });
//            }





        });
    }
}