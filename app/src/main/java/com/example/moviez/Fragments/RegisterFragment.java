package com.example.moviez.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;
import java.util.UUID;

public class RegisterFragment extends AppFragment {

    public static final String PREF_FILE_NAME = "MySharedFile";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Uri uriProfilePic;
    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private ImageView profilePic;
    private Button setImageProfile;

    public RegisterFragment() { }

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        username = view.findViewById(R.id.usernameMyUserHolder);
        email = view.findViewById(R.id.mail);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm);
        profilePic = view.findViewById(R.id.profilePic);
        Button register = view.findViewById(R.id.buyButton);
        setImageProfile = view.findViewById(R.id.setImageProfile);

        final ActivityResultLauncher<String> phoneGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                setImageProfile.setAlpha(0f);
                uriProfilePic = uri;
                profilePic.setImageURI(uri);
                appViewModel.setUriImagenSeleccionada(uri);
            } else {
                uriProfilePic = null;
                Glide.with(profilePic).load(R.drawable.ic_baseline_person_24);
            }
        });

        setImageProfile.setOnClickListener(v -> phoneGallery.launch("image/*"));

        register.setOnClickListener(v -> {
            if (validateData()) {
                registerNewUser();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.landingFrame, fragment)
                    .commit();
        }
    }

    public boolean containsUpperCaseLetter(String s) {
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

    public boolean validateData() {
        String usernameValue = Objects.requireNonNull(username.getText()).toString();
        String emailValue = Objects.requireNonNull(email.getText()).toString();
        String passwordValue = Objects.requireNonNull(password.getText()).toString();
        String confirmValue = Objects.requireNonNull(confirmPassword.getText()).toString();

        if (usernameValue.matches("") || emailValue.matches("") || passwordValue.matches("") || confirmValue.matches("")) {
            Toast.makeText(getContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
        } else if (!emailValue.contains("@")) {
            Toast.makeText(getContext(), "El correo debe contener una @", Toast.LENGTH_SHORT).show();
        } else if (!containsUpperCaseLetter(passwordValue)) {
            Toast.makeText(getContext(), "La contraseña debe contener almenos una mayúscula", Toast.LENGTH_SHORT).show();
        } else if (!containsNumber(passwordValue)) {
            Toast.makeText(getContext(), "La contraseña debe contener almenos un número", Toast.LENGTH_SHORT).show();
        } else if (passwordValue.length() < 8) {
            Toast.makeText(getContext(), "La contraseña debe contener almenos 8 carácteres", Toast.LENGTH_SHORT).show();
        } else if (!passwordValue.equals(confirmValue)) {
            Toast.makeText(getContext(), "Los campos de contraseña deben coincidir", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    private void registerNewUser() {

        String usernameValue = Objects.requireNonNull(username.getText()).toString();
        String emailValue = Objects.requireNonNull(email.getText()).toString();
        String passwordValue = Objects.requireNonNull(password.getText()).toString();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailValue, passwordValue).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (uriProfilePic != null) {
                    FirebaseStorage.getInstance().getReference("/profileimgs/" + UUID.randomUUID() + ".jpg")
                            .putFile(uriProfilePic)
                            .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                            .addOnSuccessListener(imageUrl -> saveUser(Objects.requireNonNull(auth.getCurrentUser()).getUid(), usernameValue, emailValue, passwordValue, imageUrl));
                } else {
                    saveUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), usernameValue, emailValue, passwordValue, null);
                }
                Toast.makeText(requireContext(), "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                setFragment(new LoginFragment());
            } else {
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUser(String userid, String usernameValue, String emailValue, String passwordValue, Uri imageUri) {

        String imageUrl;

        if (imageUri == null || imageUri.toString().equals("")) {
            imageUrl = "";
        } else {
            imageUrl = imageUri.toString();
        }

        Models.User userToAdd = new Models.User(userid, usernameValue, emailValue, passwordValue, imageUrl);

        db.collection("users")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid())
                .set(userToAdd);


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Objects.requireNonNull(username.getText()).toString())
                .setPhotoUri(Uri.parse(imageUrl))
                .build();
        auth.getCurrentUser().updateProfile(profileUpdates);
        appViewModel.userlogged = userToAdd;
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