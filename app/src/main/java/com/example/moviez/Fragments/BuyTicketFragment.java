package com.example.moviez.Fragments;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
    List<Models.Film> allFilms = new ArrayList<>();
    List<String> filmsNamesToShow = new ArrayList<>();
    List<String> cinemasNamesToShow = new ArrayList<>();

    Models.Cinema selectedCinema = new Models.Cinema();

    boolean isFilmAvailable = false;

    Spinner spinnerCinema;
    TextView titleFilm;
    TextView movieSinopsis;
    TextView movieDuration;
    ImageView movieImage;
    EditText dateInput;
    Spinner spinnerHour;
    Spinner spinnerMovie;
    Button buyButton;

    DatePickerDialog picker;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Models.Film filmSelected = new Models.Film();
    Models.Cinema cinemaSelected = new Models.Cinema();

    String cinemaId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<String> sessionsToShow = new ArrayList<>();

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


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);

        db.collection("movie_sessions").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Models.Film film = documentSnapshot.toObject(Models.Film.class);
                allFilms.add(film);
                filmsNamesToShow.add(film.title);
            }

            ArrayAdapter<String> filmAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, filmsNamesToShow);

            spinnerMovie.setAdapter(filmAdapter);
            filmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (filmId != 0) {
//            Get the film with the id = filmId iterating over the list of films:
                for (Models.Film film : allFilms) {
                    if (film.getId() == filmId) {
                        filmSelected = film;
                    }
                }
            } else {
                filmSelected = allFilms.get(0);
            }

            adaptFilmToLayout(filmSelected);

            spinnerMovie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(filmId == 0) { ;
                        filmSelected = allFilms.get(position);
                    } else {
                        filmId = 0;
                    }
                    adaptFilmToLayout(filmSelected);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    filmSelected = allFilms.get(0);
                }
            });
            adaptFilmToLayout(filmSelected);
            spinnerMovie.setSelection(filmsNamesToShow.indexOf(filmSelected.title));
        });


        dateInput.setOnClickListener(view1 -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            picker = new DatePickerDialog(requireContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            dateInput.setText(i2 + "-" + (i1 + 1) + "-" + i);
                            sessionsToShow.clear();

                            db.collection("movie_sessions").document(filmSelected.id + "").collection("cinemas").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    db.collection("movie_sessions").document(filmSelected.id + "").collection("cinemas").document(documentSnapshot.getId()).collection("rooms").get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        for (DocumentSnapshot documentSnapshot1 : queryDocumentSnapshots1.getDocuments()) {
                                            db.collection("movie_sessions").document(filmSelected.id + "").collection("cinemas").document(documentSnapshot.getId()).collection("rooms").document(documentSnapshot1.getId()).collection("sessions").get().addOnSuccessListener(queryDocumentSnapshots2 -> {
                                                for (DocumentSnapshot documentSnapshot2 : queryDocumentSnapshots2.getDocuments()) {
                                                    Models.Session session = documentSnapshot2.toObject(Models.Session.class);

//                                                    Split session.sessionid to get date and time:
                                                        String[] sessionIdSplit = session.sessionid.split("-");
                                                        String sessionMonth = sessionIdSplit[0];
                                                        String sessionDay = sessionIdSplit[1];

                                                        String dateSession = sessionDay + "-" + sessionMonth + "-" + LocalDate.now().getYear();

                                                        if (dateSession.equals(dateInput.getText().toString())) {
                                                            sessionsToShow.add(session.time + ":00");
                                                        }
                                                    }
                                                ArrayAdapter<String> sessionAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sessionsToShow);
                                                sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                spinnerHour.setAdapter(sessionAdapter);
                                            });
                                        }
                                    });
                                }

                            });

                        }
                    }, year, month, day);

            picker.show();
        });


        db.collection("cinemas").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                Models.Cinema cinema = documentSnapshot.toObject(Models.Cinema.class);
                allCinemas.add(cinema);
                cinemasNamesToShow.add(cinema.name);
            }

            spinnerCinema.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    for(Models.Cinema cinema : allCinemas) {
                        if(cinema.name.equals(spinnerCinema.getSelectedItem().toString())) {
                            cinemaId = cinema.cinemaid;
                            Toast.makeText(getContext(), cinemaId, Toast.LENGTH_SHORT).show();
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
        });
        buyButton.setOnClickListener(view1 -> {
            setFragment(new SeatsFragment());
        });
    }

    private void adaptFilmToLayout(Models.Film filmSelected) {
        titleFilm.setText(filmSelected.title);
        movieSinopsis.setText(filmSelected.overview);
        movieDuration.setText(filmSelected.runtime / 60 + "h " + filmSelected.runtime % 60 + "min");
        Glide.with(requireContext()).load("https://image.tmdb.org/t/p/original" + filmSelected.poster_path).into(movieImage);
    }

    private void hook(View view) {
        spinnerCinema = view.findViewById(R.id.spinnerCinema);
        dateInput = view.findViewById(R.id.dateInput);
        spinnerHour = view.findViewById(R.id.spinnerHour);
        spinnerMovie = view.findViewById(R.id.spinnerMovie);
        buyButton = view.findViewById(R.id.buyButton);
        titleFilm = view.findViewById(R.id.titleFilm);
        movieSinopsis = view.findViewById(R.id.movieSinopsis);
        movieDuration = view.findViewById(R.id.movieDuration);
        movieImage = view.findViewById(R.id.movieImage);

    }
    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }
}