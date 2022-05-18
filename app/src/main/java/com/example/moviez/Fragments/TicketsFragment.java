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

    private static final int CAMERA_REQUEST_CODE = 101;

    public TextView noTickets;

    public int frameComingFrom = 0;


    public static RecyclerView recyclerTickets;
    public static LinearLayout linearPages;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Models.Ticket> tickets = new ArrayList<>();
    public FloatingActionButton button;

    public TicketsFragment() {
        // Required empty public constructor
    }

    public TicketsFragment(int frameComingFrom) {
        this.frameComingFrom = frameComingFrom;
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
        button.setOnClickListener(v -> {
            askCameraPermission();
            setFragment(new QRScanFragment(frameComingFrom));
        });
        getTicketsFromFirebase();
    }

    private void getTicketsFromFirebase() {
        db.collection("users").document(auth.getCurrentUser().getUid()).collection("tickets").get().addOnSuccessListener(collection -> {
            tickets.clear();
            if(!collection.isEmpty()) {
                for (DocumentSnapshot document : collection.getDocuments()) {
                    if(document.toObject(Models.Ticket.class).userid.equals(auth.getCurrentUser().getUid())) {
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
            getFragmentManager()
                    .beginTransaction()
                    .replace(frameComingFrom, fragment)
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
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