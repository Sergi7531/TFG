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

import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TicketBoughtFinishedFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button goTicketButton;
    public List<Models.Ticket> ticketsToCreate;
    private String cinemaid;
    private ImageView backButton;
    private int frameComingFrom;

    public TicketBoughtFinishedFragment() { }

    public TicketBoughtFinishedFragment(List<Models.Ticket> ticketsToCreate, String cinemaid, int frameComingFrom) {
        this.frameComingFrom = frameComingFrom;
        this.ticketsToCreate = ticketsToCreate;
        this.cinemaid = cinemaid;
    }

    public static TicketBoughtFinishedFragment newInstance(String param1, String param2) {
        TicketBoughtFinishedFragment fragment = new TicketBoughtFinishedFragment();
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
        return inflater.inflate(R.layout.fragment_ticket_bought_finished, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backButton = view.findViewById(R.id.backButton3);

        for(Models.Ticket ticket : ticketsToCreate) {
            createFirebaseTicket(ticket);
            setUnavailableInSession(ticket);
        }

        backButton.setOnClickListener(v -> { backFragment(); });
        goTicketButton = view.findViewById(R.id.buyButton);
        goTicketButton.setOnClickListener(view1 -> setFragment(new TicketsFragment(frameComingFrom)));
    }

    private void setUnavailableInSession(Models.Ticket ticket) {

        String[] date = ticket.date.split("/");
        String day = date[0];
        String month = date[1];

        FirebaseFirestore.getInstance()
                .collection("movie_sessions")
                .document(ticket.filmid+"")
                .collection("cinemas")
                .document(cinemaid)
                .collection("rooms")
                .document(ticket.room+"")
                .collection("sessions")
                .document(ticket.ticketid)
                .get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()) {
                        Models.Session session = documentSnapshot.toObject(Models.Session.class);
                        if (session != null) {
                            session.seats.add(ticket.seat+(ticket.row*8));
                        }
                    }
        }).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                FirebaseFirestore.getInstance()
                        .collection("movie_sessions")
                        .document(ticket.filmid+"")
                        .collection("cinemas")
                        .document(cinemaid)
                        .collection("rooms")
                        .document(ticket.room+"")
                        .collection("sessions")
                        .document(month + "-" + day + "-" + ticket.time.substring(0,2))
                        .update("seats", FieldValue.arrayUnion(ticket.seat+(ticket.row*8)));
            }
        });
    }

    private void createFirebaseTicket(Models.Ticket ticket) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db
                .collection("users")
                .document(ticket.userid)
                .collection("tickets")
                .document(ticket.filmid + ":" + ticket.room + ":" + ticket.row + ":" + ticket.seat)
                .set(ticket);
    }

    private void backFragment() {
        getFragmentManager()
                .popBackStackImmediate();
    }

    private void setFragment(Fragment fragment) {
        if(frameComingFrom != 0) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(frameComingFrom, fragment)
                        .addToBackStack(TicketBoughtFinishedFragment.class.getSimpleName())
                        .commit();
            }
        } else {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_detail, fragment)
                        .addToBackStack(TicketBoughtFinishedFragment.class.getSimpleName())
                        .commit();
            }
        }
    }
}