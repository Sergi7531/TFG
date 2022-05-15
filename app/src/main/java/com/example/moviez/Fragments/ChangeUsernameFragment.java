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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeUsernameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUsernameFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextInputEditText newName;
    private TextInputEditText confirmName;
    private Button confirm_button;
    private ImageView goBackUsername;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChangeUsernameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeUsernameFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ViewPager2 etc...

        hook(view);

        confirm_button.setOnClickListener(v -> {
            if (validateData()) {
                changeUsername();
            }
        });

        goBackUsername.setOnClickListener(v -> {
            setFragment(new EditProfileFragment());
        });
    }

    private void changeUsername() {

        String name = newName.getText().toString();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        auth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Username updated!", Toast.LENGTH_SHORT).show();
                            System.out.println(name);
                            DocumentReference userDoc = db.collection("users").document(auth.getCurrentUser().getUid());
                            userDoc.update("username", name);

                            //      Update username in all comments:

                            db.collection("comments").get().addOnSuccessListener(queryDocumentSnapshots -> {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                                    db.collection("comments").document(documentSnapshot.getId()).collection("comments").document(auth.getCurrentUser().getUid()).addSnapshotListener((documentSnapshots, e) -> {
                                            Models.Comment comment = documentSnapshot.toObject(Models.Comment.class);
                                            comment.username = name;
                                            db.collection("comments")
                                                    .document(documentSnapshot.getId())
                                                    .collection("comments")
                                                    .document(auth.getCurrentUser().getUid())
                                                    .set(comment);
                                    });
                                }
                            });

//                            db.collection("comments").get().addOnSuccessListener(collectionDocumentSnapshots -> {
//                                for (DocumentSnapshot documentSnapshot : collectionDocumentSnapshots) {
//                                    db.collection("comments").document(documentSnapshot.getId()).collection("comments").get(auth.getCurrentUser().getUid())
//                                }
//                            });


                                    //           setFragment(new ProfileFragment());
                        }
                    }
                });

    }

    public boolean validateData() {

        String name = newName.getText().toString();
        String confirm = confirmName.getText().toString();

        if (name.matches("") || confirm.matches("")) {
            Toast.makeText(getContext(), "You need to fill all the fields!", Toast.LENGTH_SHORT).show();
        }
        else if (!name.equals(confirm)){
            Toast.makeText(getContext(), "The username field has to match with the repeat password field!", Toast.LENGTH_SHORT).show();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_username, container, false);
    }

    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }
}