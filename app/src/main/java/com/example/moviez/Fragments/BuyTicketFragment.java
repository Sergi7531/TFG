package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.moviez.Models;
import com.example.moviez.R;
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
    List<String> cinemasNamesToShow = new ArrayList<>();

    Models.Cinema selectedCinema = new Models.Cinema();

    List<String> filmsNamesToShow = new ArrayList<>();

    boolean isFilmAvailable = false;

    Spinner spinnerCinema;
    Spinner spinnerDay;
    Spinner spinnerHour;
    Spinner spinnerMovie;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String cinemaId;

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

        hook(view);

//      Consulta a firebase (coleccion cinemas):
        db.collection("cinemas").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Models.Cinema cinema = documentSnapshot.toObject(Models.Cinema.class);
                allCinemas.add(cinema);
            }

//            Adapt the spinner with the cinemas names:

            for (Models.Cinema cinema : allCinemas) {
                cinemasNamesToShow.add(cinema.name);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cinemasNamesToShow);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerCinema.setAdapter(adapter);

            spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    for(Models.Cinema cinema : allCinemas) {
                        if(cinema.name.equals(spinnerCinema.getSelectedItem().toString())) {
                            cinemaId = cinema.cinemaid;
                            Toast.makeText(getContext(), cinemaId, Toast.LENGTH_SHORT).show();
                            checkFilms(cinemaId);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    for(Models.Cinema cinema : allCinemas) {
                        if(cinema.name.equals(cinemasNamesToShow.get(0))) {
                            cinemaId = cinema.cinemaid;
                            Toast.makeText(getContext(), cinemaId, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });





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

    private void checkFilms(String cinemaId) {
//        TODO: Check if the film is in the cinema:
        //        db.collection("cinemas").document(cinemaId).collection("rooms").get().addOnSuccessListener(queryDocumentSnapshotsRooms -> {
////            For each room in the cinema, get the sessions. Then, for each session, get the film on that session.
//            for(DocumentSnapshot room : queryDocumentSnapshotsRooms.getDocuments()) {
//                db.collection("cinemas").document(cinemaId).collection("rooms").document(room.getId()).collection("sessions").get().addOnSuccessListener(queryDocumentSnapshotsSessions -> {
////                    For each session, get the film on that session:
//                    for(DocumentSnapshot session : queryDocumentSnapshotsSessions.getDocuments()) {
//                       session.toObject(Models.Session.class);
//                    }
//                });
//            }
//        });
    }

    private void hook(View view) {
        spinnerCinema = view.findViewById(R.id.spinnerCinema);
        spinnerDay = view.findViewById(R.id.spinnerDay);
        spinnerHour = view.findViewById(R.id.spinnerHour);
        spinnerMovie = view.findViewById(R.id.spinnerMovie);

    }
}