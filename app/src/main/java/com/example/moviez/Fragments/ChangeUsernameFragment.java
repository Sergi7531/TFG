package com.example.moviez.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class ChangeUsernameFragment extends AppFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextInputEditText newName;
    private TextInputEditText confirmName;
    private Button confirm_button;
    private ImageView goBackUsername;

    public ChangeUsernameFragment() { }

    public static ChangeUsernameFragment newInstance(String param1, String param2) {
        ChangeUsernameFragment fragment = new ChangeUsernameFragment();
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

        hook(view);

        confirm_button.setOnClickListener(v -> {
            if (validateData()) {
                changeUsername();
            }
        });

        goBackUsername.setOnClickListener(v -> setFragment(new EditProfileFragment()));
    }

    private void changeUsername() {

        String name = newName.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        Objects.requireNonNull(auth.getCurrentUser()).updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Username updated!", Toast.LENGTH_SHORT).show();
                        DocumentReference userDoc = db.collection("users").document(auth.getCurrentUser().getUid());

                        db.collection("comments").get().addOnSuccessListener(queryDocumentSnapshots -> {
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                                db.collection("comments").document(documentSnapshot.getId()).collection("comments").get().addOnSuccessListener(documentSnapshots -> {
                                    for (DocumentSnapshot documentSnapshot1 : documentSnapshots.getDocuments()) {
                                        if (Objects.requireNonNull(documentSnapshot1.toObject(Models.Comment.class)).userid.equals(auth.getCurrentUser().getUid())) {
                                            Models.Comment comment = documentSnapshot1.toObject(Models.Comment.class);
                                            if (comment != null) {
                                                comment.username = name;
                                            }
                                            if (comment != null) {
                                                db.collection("comments")
                                                        .document(documentSnapshot.getId())
                                                        .collection("comments")
                                                        .document(documentSnapshot1.getId())
                                                        .set(comment);
                                            }

                                        }
                                    }
                                });
                            }
                        });
                        userDoc.update("username", name);
                    }
                });
    }

    public boolean validateData() {

        String name = newName.getText().toString();
        String confirm = confirmName.getText().toString();

        if (name.matches("") || confirm.matches("")) {
            Toast.makeText(getContext(), "Por favor, llena todos los campos.", Toast.LENGTH_SHORT).show();
        }
        else if (!name.equals(confirm)){
            Toast.makeText(getContext(), "Revisa que ambos nombres estén igual escritos.", Toast.LENGTH_SHORT).show();
        }
        else {
            return true;
        }
        return false;
    }

    public void hook(View view) {
        newName = view.findViewById(R.id.newName);
        confirmName = view.findViewById(R.id.confirmName);
        confirm_button = view.findViewById(R.id.confirm_button);
        goBackUsername = view.findViewById(R.id.goBackButton);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_username, container, false);
    }

    private void setFragment(Fragment fragment) {
        if (getFragmentManager() != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
        }
    }
}