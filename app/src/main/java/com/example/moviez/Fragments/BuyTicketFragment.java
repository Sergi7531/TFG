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
import com.example.moviez.UpdateFilmsInCinemas;
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
    public static int frameComingFrom = 0;

    private static int roomsInSelectedCinema = 0;
    List<Models.Cinema> allCinemas = new ArrayList<>();
    List<Models.Film> allFilms = new ArrayList<>();
    List<String> filmsNamesToShow = new ArrayList<>();
    List<String> cinemasNamesToShow = new ArrayList<>();

    private static String selectedCinemaId = "";
    private static int selectedFilmId = 0;
    private static int selectedRoomId = 0;
    private static String selectedDate = "";

    TextView titleFilm;
    TextView movieSinopsis;
    TextView movieDuration;
    ImageView movieImage;
    EditText dateInput;
    Spinner spinnerHour;
    Spinner spinnerMovie;
    Button buyButton;

    TextView cinemaName;
    TextView roomNumber;

    DatePickerDialog picker;

    String[] maxDate = new String[3];


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Models.Film filmSelected = new Models.Film();


    // TODO: Rename and change types of parameters
    private List<String> sessionsToShow = new ArrayList<>();

    public BuyTicketFragment() {
        // Required empty public constructor
    }


    public BuyTicketFragment(int param1, int frameComingFrom) {
        this.frameComingFrom = frameComingFrom;
        filmId = param1;
    }
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
                    filmSelected = allFilms.get(position);
                    filmId = filmSelected.getId();
                    setCinemaInfo(filmId);
                    adaptFilmToLayout(filmSelected);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    filmSelected = allFilms.get(0);
                    filmId = filmSelected.getId();
                }
            });
            adaptFilmToLayout(filmSelected);
            spinnerMovie.setSelection(filmAdapter.getPosition(filmSelected.title));
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
//                            Check if the date is before appviewmodel maxium date:
//                            First, split appViewModel.maximumDate into a list of strings:\
                            UpdateFilmsInCinemas.dateEnd.observe(getViewLifecycleOwner(), s -> {
                                maxDate = s.split("-");

                                int maxYear = Integer.parseInt(maxDate[0]);
                                int maxMonth = Integer.parseInt(maxDate[1]);
                                int maxDay = Integer.parseInt(maxDate[2]);

                                LocalDate dateSelectedLocal = LocalDate.of(i, i1+1, i2);
                                LocalDate maxDateLocal = LocalDate.of(maxYear, maxMonth, maxDay);


                                if (dateSelectedLocal.isBefore(maxDateLocal)) {

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

                                                        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sessionsToShow);
                                                        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                        spinnerHour.setAdapter(sessionAdapter);
                                                    });
                                                }
                                            });
                                        }

                                    });

                                } else {
                                    Toast.makeText(getContext(), "La película no se emitirá en los cines este día.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, year, month, day);
            picker.show();
        });


        buyButton.setOnClickListener(view1 -> {

            String[] dateInputSplitted = dateInput.getText().toString().trim().split("-");

            setFragment(new SeatsFragment(frameComingFrom, selectedFilmId, selectedCinemaId, selectedRoomId, Integer.parseInt(dateInputSplitted[0]), Integer.parseInt(dateInputSplitted[1]), Integer.parseInt(spinnerHour.getSelectedItem().toString().substring(0,2))));
        });
    }

    private void setCinemaInfo(int filmSelected) {
        allCinemas.clear();
        cinemasNamesToShow.clear();
        if(filmSelected == 0) {
            db.collection("cinemas").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    Models.Cinema cinema = documentSnapshot.toObject(Models.Cinema.class);
                    allCinemas.add(cinema);
                }
            });
        } else {
            db.collection("movie_sessions").document(String.valueOf(filmSelected)).collection("cinemas").get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
//                    Set the cinemaName text:
                    selectedCinemaId = documentSnapshot.toObject(Models.Cinema.class).cinemaid;
                    cinemaName.setText(documentSnapshot.toObject(Models.Cinema.class).name);
                    db.collection("movie_sessions").document(String.valueOf(filmSelected)).collection("cinemas").document(documentSnapshot.toObject(Models.Cinema.class).cinemaid).collection("rooms").get().addOnSuccessListener(documentSnapshot1 -> {
                        selectedFilmId = documentSnapshot1.getDocuments().get(0).toObject(Models.Room.class).filmid;
                        selectedRoomId = documentSnapshot1.getDocuments().get(0).toObject(Models.Room.class).roomid;
                        roomNumber.setText(documentSnapshot1.getDocuments().get(0).toObject(Models.Room.class).name);
                    });
                }
            });
        }
    }

    private void adaptFilmToLayout(Models.Film filmSelected) {
        titleFilm.setText(filmSelected.title);
        movieSinopsis.setText(filmSelected.overview);
        movieDuration.setText(filmSelected.runtime / 60 + "h " + filmSelected.runtime % 60 + "min");
        Glide.with(requireContext()).load("https://image.tmdb.org/t/p/original" + filmSelected.poster_path).into(movieImage);
    }

    private void hook(View view) {
        dateInput = view.findViewById(R.id.dateInput);
        spinnerHour = view.findViewById(R.id.spinnerHour);
        spinnerMovie = view.findViewById(R.id.spinnerMovie);
        buyButton = view.findViewById(R.id.buyButton);
        titleFilm = view.findViewById(R.id.titleFilm);
        movieSinopsis = view.findViewById(R.id.movieSinopsis);
        movieDuration = view.findViewById(R.id.movieDuration);
        movieImage = view.findViewById(R.id.movieImage);
        cinemaName = view.findViewById(R.id.cinemaName);
        roomNumber = view.findViewById(R.id.roomNumber);
    }
    private void setFragment(Fragment fragment) {
        if(frameComingFrom != 0) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(frameComingFrom, fragment)
                    .addToBackStack(BuyTicketFragment.class.getSimpleName())
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_detail, fragment)
                    .addToBackStack(BuyTicketFragment.class.getSimpleName())
                    .commit();
        }

    }
}