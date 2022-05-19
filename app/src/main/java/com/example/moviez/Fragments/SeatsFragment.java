package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Adapters.SeatAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeatsFragment extends AppFragment {

    public static int frameComingFrom = 0;
    public static int movieid;
    public static String cinemaid;
    public static int roomid;
    public static int day;
    public static int month;
    public static int hour;

    Models.Cinema cinema = new Models.Cinema();
    private ImageView backButton;
    private RecyclerView seatsGrid;
    private Button buyButton2;
    List<Models.Seats> seats = new ArrayList<>();

    List<Models.Ticket> ticketsToBuy = new ArrayList<>();

    public SeatsFragment() { }

    public SeatsFragment(int frameComingFrom, int movieid, String cinemaid, int roomid, int day, int month, int hour) {
        SeatsFragment.frameComingFrom = frameComingFrom;
        SeatsFragment.movieid = movieid;
        SeatsFragment.cinemaid = cinemaid;
        SeatsFragment.roomid = roomid;
        SeatsFragment.day = day;
        SeatsFragment.month = month;
        SeatsFragment.hour = hour;
    }

    public static SeatsFragment newInstance() {
        SeatsFragment fragment = new SeatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void hook(View view) {
        seatsGrid = view.findViewById(R.id.seatsGrid);
        buyButton2 = view.findViewById(R.id.buyButton2);
        backButton = view.findViewById(R.id.backButton);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);
        insertDefaultSeats();
        backButton.setOnClickListener(view1 -> getActivity().onBackPressed());
        buyButton2.setOnClickListener(view1 -> createTicketsForSelectedSeats());
    }

    private void createTicketsForSelectedSeats() {
        for (Models.Seats seat : seats) {
            if (seat.state.equals(Models.SeatState.SELECTED)) {
                Models.Ticket ticket = new Models.Ticket();
                db.collection("cinemas").document(cinemaid).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            cinema = task.getResult().toObject(Models.Cinema.class);
                            if (cinema != null) {
                                ticket.cinemaCoords = cinema.coords;
                            }
                            if (cinema != null) {
                                ticket.cinemaName = cinema.name;
                            }
                        }
                    }
                });

                ticket.filmid = movieid;
                ticket.userid = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                ticket.date = day + "/" + month;
                ticket.time = hour + ":00";
                ticket.room = roomid;
                ticket.row = seat.row;
                ticket.seat = seat.seat;

                ticket.ticketid = movieid + ":" + roomid + ":" + seat.row + ":" + seat.seat;

                ticketsToBuy.add(ticket);
            }
        }
        TicketListBoughtFragment ticketListBoughtFragment = new TicketListBoughtFragment(ticketsToBuy, cinemaid, frameComingFrom);
        setFragment(ticketListBoughtFragment);
    }

    private void showSeats() {
        seatsGrid.setAdapter(new SeatAdapter(seats, requireContext()));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 8, RecyclerView.VERTICAL, false);
        seatsGrid.setLayoutManager(gridLayoutManager);
        seatsGrid.setNestedScrollingEnabled(false);
    }


    private void insertDefaultSeats() {
        db.collection("movie_sessions").
                document(movieid + "").
                collection("cinemas").
                document(cinemaid + "").
                collection("rooms").
                document(roomid + "").
                collection("sessions").
                document(month + "-" + day + "-" + hour).
                get().
                addOnSuccessListener(success -> {

                Models.Session session = success.toObject(Models.Session.class);

                seats.clear();
                for (int i = 0; i < 8 ; i++) {
                    for (int j = 0; j < 8 ; j++) {
                        Models.SeatState state = Models.SeatState.FREE;
                        if (session != null && session.seats.contains(i * 8 + j)) {
                            state = Models.SeatState.BUSY;
                        }
                        seats.add(new Models.Seats(i, j, state));
                    }
                }
                showSeats();
            });
    }

    private void setFragment(Fragment fragment) {
        if (frameComingFrom != 0) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(frameComingFrom, fragment)
                        .addToBackStack(SeatsFragment.class.getSimpleName())
                        .commit();
            }
        } else {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_detail, fragment)
                        .addToBackStack(SeatsFragment.class.getSimpleName())
                        .commit();
            }
        }
    }
}