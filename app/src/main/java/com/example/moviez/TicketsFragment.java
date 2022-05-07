package com.example.moviez;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketsFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    public static ViewPager2 viewPagerTickets;
    public static TicketsAdapter adapterTickets;

    public static ImageView imageTicketDetail;
    public static TextView filmNameTicketDetail;
    public static TextView taglineTicket;
    public static TextView cinemaNameTicketDetail;
    public static TextView dayTicketDetail;
    public static TextView timeTicketDetail;
    public static TextView durationTicket;
    public static TextView roomTicketDetail;
    public static TextView rowTicketDetail;
    public static TextView seatTicketDetail;
    public static TextView seatTicketDetail;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Models.Ticket> tickets = new ArrayList<>();

    public TicketsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketsFragment newInstance(String param1, String param2) {
        TicketsFragment fragment = new TicketsFragment();
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
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ViewPager2 etc...

        hook(view);
        getTicketsFromFirebase();
    }

    private void getTicketsFromFirebase() {
        db.collection("users").document(appViewModel.userlogged.userid).collection("tickets").get().addOnSuccessListener(collection -> {
            tickets.clear();
            if(!collection.isEmpty()) {
                for (DocumentSnapshot document : collection.getDocuments()) {
                    tickets.add(document.toObject(Models.Ticket.class));
                }
                adapterTickets = new TicketsAdapter(tickets, requireContext());
                viewPagerTickets.setAdapter(adapterTickets);
            }
        });
    }

    private void hook(View view) {
        viewPagerTickets = view.findViewById(R.id.viewPagerTickets);
        imageTicketDetail = view.findViewById(R.id.imageTicketDetail);
        filmNameTicketDetail = view.findViewById(R.id.filmNameTicketDetail);
        taglineTicket = view.findViewById(R.id.taglineTicket);
        cinemaNameTicketDetail = view.findViewById(R.id.cinemaNameTicketDetail);
        dayTicketDetail = view.findViewById(R.id.dayTicketDetail);
        timeTicketDetail = view.findViewById(R.id.durationTicket);
        durationTicket = view.findViewById(R.id.durationTicket);
        roomTicketDetail = view.findViewById(R.id.roomTicketDetail);
        rowTicketDetail = view.findViewById(R.id.rowTicketDetail);
        seatTicketDetail = view.findViewById(R.id.seatTicketDetail);


    }
}