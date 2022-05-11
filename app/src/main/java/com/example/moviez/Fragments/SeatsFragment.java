package com.example.moviez.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.moviez.Adapters.SeatAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView seatsGrid;
    private Button buyButton2;
    List<Models.Seats> seats = new ArrayList<>();

    public SeatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeatsFragment newInstance(String param1, String param2) {
        SeatsFragment fragment = new SeatsFragment();
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

    private void hook(View view) {
        seatsGrid = view.findViewById(R.id.seatsGrid);
        buyButton2 = view.findViewById(R.id.buyButton2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);
        insertDefaultSeats();
        showSeats();
        buyButton2.setOnClickListener(view1 -> {
            setFragment(new TicketListBoughtFragment());
            //TODO
            //Set seats to busy state
        });
    }

    private void showSeats() {
        seatsGrid.setAdapter(new SeatAdapter(seats, requireContext()));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 8, RecyclerView.VERTICAL, false);
        seatsGrid.setLayoutManager(gridLayoutManager);
        seatsGrid.setNestedScrollingEnabled(false);
    }


    private void insertDefaultSeats() {
        seats.clear();
        for (int i = 0; i < 8 ; i++){
            for (int j = 0; j < 8 ; j++){
                seats.add(new Models.Seats("SeatID" + i + j, i, j));
            }
        }
    }
    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }

}