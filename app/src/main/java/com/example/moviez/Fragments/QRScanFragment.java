package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.Objects;

public class QRScanFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private CodeScanner codeScanner;

    public int frameComingFrom = 0;

    public QRScanFragment() { }

    public QRScanFragment(int frameComingFrom) {
        this.frameComingFrom = frameComingFrom;
    }

    public static QRScanFragment newInstance(String param1, String param2) {
        QRScanFragment fragment = new QRScanFragment();
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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        openCamera(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    public void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_q_r_scan, container, false);
    }

    private void openCamera(View view) {

        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getContext(), scannerView);
        codeScanner.setDecodeCallback(result -> getActivity().runOnUiThread(() -> {

            String[] resultSplitted = result.getText().split(";;");

            Models.Ticket ticket = new Models.Ticket();

            if(resultSplitted.length == 14) {
                ticket.ticketid = resultSplitted[0];
                ticket.filmid = Integer.parseInt(resultSplitted[1]);
                ticket.userid = resultSplitted[2];
                ticket.filmName = resultSplitted[3];
                ticket.tagline = resultSplitted[4];
                ticket.filmImage = resultSplitted[5];
                ticket.cinemaName = resultSplitted[6];
                ticket.cinemaCoords = resultSplitted[7];
                ticket.date = resultSplitted[8];
                ticket.time = resultSplitted[9];
                ticket.duration = Integer.parseInt(resultSplitted[10]);
                ticket.row = Integer.parseInt(resultSplitted[11]);
                ticket.seat = Integer.parseInt(resultSplitted[12]);
                ticket.room = Integer.parseInt(resultSplitted[13]);

                db.collection("users").document(ticket.userid).collection("tickets").document(ticket.ticketid).delete().addOnSuccessListener(success1 -> {

                    ticket.userid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                    db.collection("users").document(ticket.userid).collection("tickets").document(ticket.ticketid).set(ticket).addOnSuccessListener(success -> {
                    });


                });
                scannerView.setOnClickListener(view1 -> codeScanner.startPreview());
            } else {
                Toast.makeText(requireContext(), "QR no válido.", Toast.LENGTH_SHORT).show();
            }
            setFragment();
        }));
    }


    private void setFragment() {
        if(frameComingFrom != 0) {
            navigateWithFrame(frameComingFrom);
        } else {
            navigateWithFrame(R.id.main_frame);
        }
    }

    private void navigateWithFrame(int frame) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = null;
        if (fragmentManager != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
        }
        TicketsFragment ticketsFragment = new TicketsFragment();
        if (fragmentTransaction != null) {
            fragmentTransaction.replace(R.id.main_frame, ticketsFragment);
        }
        fragmentTransaction.replace(frame, ticketsFragment);
        fragmentTransaction.commit();
    }
}