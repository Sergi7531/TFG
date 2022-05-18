package com.example.moviez.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;


public class PasswordFragment extends AppFragment {

    public Button recoverPassword;
    public TextInputEditText mailRecovery;
    public ImageView goBackPassword;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public PasswordFragment() {
    }

    public static PasswordFragment newInstance(String param1, String param2) {
        PasswordFragment fragment = new PasswordFragment();
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
        return inflater.inflate(R.layout.fragment_password, container, false);
    }

    private void setFragmentMain(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
        }
    }
    private void setFragmentLanding(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.landingFrame, fragment)
                    .commit();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        hook(view);
        String activity_type = getActivity().getClass().getSimpleName();
        goBackPassword.setOnClickListener(v -> {

            if (activity_type.equals("LandingActivity")){
                setFragmentLanding(new LoginFragment());
            } else {
                setFragmentMain(new EditProfileFragment());
            }
        });

        recoverPassword.setOnClickListener(view1 -> {
            if(auth.getCurrentUser() != null) {
                if(auth.getCurrentUser().getEmail().equals(mailRecovery.getText().toString().trim())) {
                    auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail());
                    setFragmentMain(new EditProfileFragment());
                    Toast.makeText(requireContext(), "Correo mandado con éxito.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Introduce tu correo actual", Toast.LENGTH_SHORT).show();
                }
            } else {
                auth.sendPasswordResetEmail(mailRecovery.getText().toString().trim());
                setFragmentLanding(new LoginFragment());
                Toast.makeText(requireContext(), "Correo mandado con éxito.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hook(View view) {
        recoverPassword = view.findViewById(R.id.recoverPassword);
        mailRecovery = view.findViewById(R.id.mail);
        goBackPassword = view.findViewById(R.id.goBackPassword);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setFragmentLanding(new LoginFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

}