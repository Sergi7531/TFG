package com.example.moviez.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.zxing.Result;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QRScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class QRScanFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CAMERA_REQUEST_CODE = 101;
    private CodeScanner codeScanner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QRScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRScanFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        askCameraPermission(view);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_q_r_scan, container, false);
    }

    private void openCamera(View view) {
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getContext(), scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String[] resultSplitted = result.getText().split(";;");

                        Models.Ticket ticket = new Models.Ticket();

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

//                            When the ticket is added, we need to remove it from the user with the ticket.userid:

                        db.collection("users").document(ticket.userid).collection("tickets").document(ticket.ticketid).delete().addOnSuccessListener(success1 -> {

//                            Set the ticket to the user with the ticket.userid:

                            ticket.userid = auth.getCurrentUser().getUid();


                            db.collection("users").document(ticket.userid).collection("tickets").document(ticket.ticketid).set(ticket).addOnSuccessListener(success -> {

                            });

//                            Go back:
                            setFragment();
                        });
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });
    }

    private void setFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TicketsFragment ticketsFragment = new TicketsFragment();
        fragmentTransaction.replace(R.id.main_frame, ticketsFragment);
        fragmentTransaction.commit();
    }

    private void askCameraPermission(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                        CAMERA_REQUEST_CODE);
            } else {
                openCamera(view);
            }
        }
        else {
            openCamera(view);
        }
    }
}