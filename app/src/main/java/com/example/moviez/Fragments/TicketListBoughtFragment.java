package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Adapters.BuyTicketAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketListBoughtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketListBoughtFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Models.Ticket> ticketsToBuy = new ArrayList<>();
    private String cinemaid;
    private int frameComingFrom = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button goTicketButton;
    private RecyclerView ticketRecycler;
    private ImageView backButton;
    private TextView numberTicketsText;
    private TextView totalPriceTickets;


    public TicketListBoughtFragment() {
        // Required empty public constructor
    }

    public TicketListBoughtFragment(List<Models.Ticket> ticketsToBuy, String cinemaid, int frameComingFrom) {
        this.frameComingFrom = frameComingFrom;
        this.ticketsToBuy = ticketsToBuy;
        this.cinemaid = cinemaid;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TicketListBoughtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketListBoughtFragment newInstance(String param1, String param2) {
        TicketListBoughtFragment fragment = new TicketListBoughtFragment();
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
        return inflater.inflate(R.layout.fragment_ticket_list_bought, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);
        adaptTickets();
        numberTicketsText.setText(String.valueOf(ticketsToBuy.size()));
        totalPriceTickets.setText(String.valueOf(ticketsToBuy.size()*10));

        goTicketButton.setOnClickListener(view1 -> {
            setFragment(new TicketBoughtFinishedFragment(ticketsToBuy, cinemaid, frameComingFrom));
        });
    }

    private void adaptTickets() {
        ticketRecycler.setAdapter(new BuyTicketAdapter(requireContext(), ticketsToBuy, TicketListBoughtFragment.this));
        ticketRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void hook(View view) {
        goTicketButton = view.findViewById(R.id.buyButton);
        ticketRecycler = view.findViewById(R.id.ticketRecycler);
        backButton = view.findViewById(R.id.backButton);
        numberTicketsText = view.findViewById(R.id.numberTicketsText);
        totalPriceTickets = view.findViewById(R.id.totalPriceTickets);
    }

    private void setFragment(Fragment fragment) {
        if(frameComingFrom != 0) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(frameComingFrom, fragment)
                    .addToBackStack(TicketListBoughtFragment.class.getSimpleName())
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_detail, fragment)
                    .addToBackStack(TicketListBoughtFragment.class.getSimpleName())
                    .commit();
        }
    }
}