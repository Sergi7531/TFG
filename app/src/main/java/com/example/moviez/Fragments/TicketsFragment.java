package com.example.moviez.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.moviez.Adapters.TicketsAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TicketsFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int CAMERA_REQUEST_CODE = 101;

    public TextView noTickets;

    public RecyclerView recyclerTickets;
    public LinearLayout linearPages;
    public int frameComingFrom = 0;

    private final List<Models.Ticket> tickets = new ArrayList<>();
    public FloatingActionButton button;

    public TicketsFragment() { }

    public TicketsFragment(int frameComingFrom) {
        this.frameComingFrom = frameComingFrom;
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tickets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);
        button.setOnClickListener(v -> {
            askCameraPermission();
            setFragment(new QRScanFragment(frameComingFrom));
        });
        getTicketsFromFirebase();
    }

    private void getTicketsFromFirebase() {
        db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("tickets").get().addOnSuccessListener(collection -> {
            tickets.clear();
            if(!collection.isEmpty()) {
                for (DocumentSnapshot document : collection.getDocuments()) {
                    if(Objects.requireNonNull(document.toObject(Models.Ticket.class)).userid.equals(auth.getCurrentUser().getUid())) {
                        tickets.add(document.toObject(Models.Ticket.class));
                        ImageView imageView = new ImageView(getContext());
                        imageView.setImageResource(R.drawable.ic_baseline_circle_24);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
                        linearPages.addView(imageView);
                    }
                }

                if(tickets.size() == 0) {
                    noTickets.setVisibility(View.VISIBLE);
                } else {
                    noTickets.setVisibility(View.GONE);
                    recyclerTickets.setAdapter(new TicketsAdapter(tickets, requireContext()));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                    SnapHelper snapHelper = new PagerSnapHelper();
                    recyclerTickets.setLayoutManager(layoutManager);
                    snapHelper.attachToRecyclerView(recyclerTickets);
                }
            }
        });
    }

    private void hook(View view) {
        recyclerTickets = view.findViewById(R.id.recyclerTickets);
        linearPages = view.findViewById(R.id.linearPages);
        button = view.findViewById(R.id.qrButton);
        noTickets = view.findViewById(R.id.noTickets);
    }

    private void setFragment(Fragment fragment) {
        if(frameComingFrom != 0) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(frameComingFrom, fragment)
                        .commit();
            }
        } else {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame, fragment)
                        .commit();
            }
        }
    }

    private void askCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            } else {
                setFragment(new QRScanFragment(frameComingFrom));
            }
        }
        else {
            setFragment(new QRScanFragment(frameComingFrom));
        }
    }
}