package com.example.moviez.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Adapters.BuyTicketAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TicketListBoughtFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    STRIPE CONFIG VARIABLES:
    private static final String CURRENT_STATE_APP = "PRODUCTION";
    private static final String PUBLISHABLE_KEY = "pk_test_51L1cHSIXjyZSXV5IA52njaxxUaML0iT59J4DU3NNtfxrJ9hYKrMXa4ZfCy9tSMXt5NIKeaQIjjGocJOFBhNtwTiw00X1SQtO6J";
    private String clientSecret = "sk_test_51L1cHSIXjyZSXV5Ivz2ueZkBQj2xiKYPMq9g7IBnb8qtJo6G5jpSqvDdgLcklhVy03ujHdV5EbrD6LgjN9N8JY9200wXdn2Oh0";
//    private PaymentLauncher paymentLauncher;

//    PaymentButton payPalButton;


    private List<Models.Ticket> ticketsToBuy = new ArrayList<>();
    private String cinemaid;
    private int frameComingFrom = 0;

    public static Button goTicketButton;
    private RecyclerView ticketRecycler;
    private ImageView backButton;
    private TextView numberTicketsText;
    private TextView totalPriceTickets;
//    private CardInputWidget cardInputWidget;

    public TicketListBoughtFragment() {}

    public TicketListBoughtFragment(List<Models.Ticket> ticketsToBuy, String cinemaid, int frameComingFrom) {
        this.frameComingFrom = frameComingFrom;
        this.ticketsToBuy = ticketsToBuy;
        this.cinemaid = cinemaid;
    }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_list_bought, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);
        adaptTickets();
        numberTicketsText.setText(String.valueOf(ticketsToBuy.size()));
        totalPriceTickets.setText(String.valueOf(ticketsToBuy.size() * 10));

        PayPalConfiguration config = new PayPalConfiguration()

//              Mock environment:
                .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

                .clientId("AdXipsWWcX1eflD7kWWwGBp5EnRASB8hUG3vUkGJg4kPevHPF1L4cWMnd7IQ8usU9BH9kBnWLCpGtgbU");

        // Create a PayPalPayment

        backButton.setOnClickListener(v -> { backFragment(); });

        goTicketButton.setOnClickListener(V -> {
            if (ticketsToBuy.size() > 0) {
                onBuyPressed(view, config);
            }
        });

        Intent intent = new Intent(requireActivity(), PayPalService.class);


        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        requireActivity().startService(intent);

    }

    private void backFragment() {
        getFragmentManager()
                .popBackStackImmediate();
    }

    //-------------------------------- START PAYPAL SERVICES --------------------------------------------------

    @Override
    public void onDestroy() {
        requireActivity().stopService(new Intent(requireActivity(), PayPalService.class));
        super.onDestroy();
    }

    public void onBuyPressed(View pressed, PayPalConfiguration config) {

        PayPalPayment payment = new PayPalPayment(new BigDecimal(10*ticketsToBuy.size()), "EUR", ticketsToBuy.size() + " entradas para " + ticketsToBuy.get(0).filmName,
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(requireActivity(), PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    Toast.makeText(requireContext(), "Compra realizada con éxito.", Toast.LENGTH_SHORT).show();
                    setFragment(new TicketBoughtFinishedFragment(ticketsToBuy, cinemaid, frameComingFrom));

                } catch (JSONException e) {
                    Toast.makeText(requireContext(), "Ha ocurrido un problema inesperado con tu compra.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(requireContext(), "Tu compra ha sido cancelada.", Toast.LENGTH_SHORT).show();
        }
        else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(requireContext(), "Los datos de pago no son correctos.", Toast.LENGTH_SHORT).show();
        }
    }

//-------------------------------- END PAYPAL SERVICES --------------------------------------------------

    private void adaptTickets() {
        ticketRecycler.setAdapter(new BuyTicketAdapter(requireContext(), ticketsToBuy, TicketListBoughtFragment.this));
        ticketRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void hook(View view) {
        goTicketButton = view.findViewById(R.id.buyButton);
        ticketRecycler = view.findViewById(R.id.ticketRecycler);
        backButton = view.findViewById(R.id.backButton2);
        numberTicketsText = view.findViewById(R.id.numberTicketsText);
        totalPriceTickets = view.findViewById(R.id.totalPriceTickets);
    }

    private void setFragment(Fragment fragment) {
        if(frameComingFrom != 0) {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(frameComingFrom, fragment)
                        .addToBackStack(TicketListBoughtFragment.class.getSimpleName())
                        .commit();
            }
        } else {
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_detail, fragment)
                        .addToBackStack(TicketListBoughtFragment.class.getSimpleName())
                        .commit();
            }
        }
    }
}