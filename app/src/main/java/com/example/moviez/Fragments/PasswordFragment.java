package com.example.moviez;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviez.Fragments.AppFragment;
import com.example.moviez.Fragments.LoginFragment;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class PasswordFragment extends AppFragment {

    public Button recoverPassword;
    public TextInputEditText mailRecovery;

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
    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.landingFrame, fragment)
                .commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recoverPassword = view.findViewById(R.id.recoverPassword);
        mailRecovery = view.findViewById(R.id.mail);


        recoverPassword.setOnClickListener(view1 -> {
            if(auth.getCurrentUser() != null) {
                if(auth.getCurrentUser().getEmail().equals(mailRecovery.getText().toString().trim())) {
                    auth.sendPasswordResetEmail(auth.getCurrentUser().getEmail());
                    setFragment(new LoginFragment());
                }
            } else {
                auth.sendPasswordResetEmail(mailRecovery.getText().toString().trim());
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                setFragment(new LoginFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

}