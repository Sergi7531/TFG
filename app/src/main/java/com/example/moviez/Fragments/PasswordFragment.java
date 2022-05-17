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
import androidx.fragment.app.FragmentActivity;

import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PasswordFragment extends AppFragment {

    public Button recoverPassword;
    public TextInputEditText mailRecovery;
    public ImageView goBackPassword;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password, container, false);
    }
    private void setFragmentMain(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }
    private void setFragmentLanding(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.landingFrame, fragment)
                .commit();
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