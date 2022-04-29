package com.example.moviez;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private ImageView profilePic;
    private Button register;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.mail);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm);
        profilePic = view.findViewById(R.id.profilePic);
        register = view.findViewById(R.id.register);

        register.setOnClickListener(v -> {
            String usernameValue = username.getText().toString();
            String emailValue = email.getText().toString();
            String passwordValue = password.getText().toString();
            String confirmValue = confirmPassword.getText().toString();

            if (usernameValue.matches("") || emailValue.matches("") || passwordValue.matches("") || confirmValue.matches("")) {
                Toast.makeText(getContext(), "You need to fill all the fields!", Toast.LENGTH_SHORT).show();
            }
            if (!emailValue.contains("@")) {
                Toast.makeText(getContext(), "The email has to contain a @", Toast.LENGTH_SHORT).show();
            }

            if (!containsUpperCaseLetter(passwordValue)) {
                Toast.makeText(getContext(), "The password has to contain a capital letter!", Toast.LENGTH_SHORT).show();
            }

            if (!containsNumber(passwordValue)) {
                Toast.makeText(getContext(), "The password has to contain a number!", Toast.LENGTH_SHORT).show();
            }

            if (!passwordValue.equals(confirmValue)) {
                Toast.makeText(getContext(), "The password field has to match with the repeat password field!", Toast.LENGTH_SHORT).show();
            }

            if (passwordValue.length() < 8) {
                Toast.makeText(getContext(), "The password must have at least 8 characters!", Toast.LENGTH_SHORT).show();
            }

            else {
                setFragment(new PreferencesFragment());
            }
        });

        final ActivityResultLauncher<String> phoneGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                //appViewModel.setUriImagenSeleccionada(uri);
            }
        });

        profilePic.setOnClickListener(v -> {
            phoneGallery.launch("image/*");
        });
    }

    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.landingFrame, fragment)
                .commit();

    }

    public boolean containsUpperCaseLetter(String s){
        for (int i = 0; i < s.length(); i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean containsNumber(String string) {
        for (int i = 0; i < string.length(); i++) {
            if (Character.isDigit(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}