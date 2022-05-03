package com.example.moviez;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public TextInputEditText usernameLog;
    public TextInputEditText passwordLog;
    public Button logButton;
    public TextView registerText;
    public CardView googleButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hook(view);
        ActivityResultLauncher<Intent> signInClient = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        try {
                            firebaseAuthWithGoogle(GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult(ApiException.class));
                        } catch (ApiException e) {

                        }
                    }
                });
        registerText.setOnClickListener(view2 -> {
            setFragment(new RegisterFragment());
        });
        GoogleSignInClient googleSignInAccount = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build());
        firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(requireContext()));
        logButton.setOnClickListener(view2 -> {
            if (!usernameLog.getText().toString().isEmpty() && !passwordLog.getText().toString().isEmpty()){

            }
        });
        googleButton.setOnClickListener(view1 -> {
            signInClient.launch(googleSignInAccount.getSignInIntent());
        });

    }

    private void hook(View view) {
        usernameLog = view.findViewById(R.id.usernameLog);
        passwordLog = view.findViewById(R.id.passwordLog);
        registerText = view.findViewById(R.id.registerText);
        logButton = view.findViewById(R.id.register);
        googleButton = view.findViewById(R.id.googleButton);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if(account == null) return;

        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        accessApp();
                    }
                });
    }
    private void accessApp(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore.collection("users").document(firebaseUser.getUid()).set(new Models.User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getEmail())).addOnCompleteListener(task -> {
             Intent intent = new Intent(requireContext(), MainActivity.class);
             startActivity(intent);
             requireActivity().finish();
        });

    }

    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.landingFrame, fragment)
                .commit();
    }
    /*
    public TextInputEditText usernameLog;
    public TextInputEditText passwordLog;
    public Button logButton;
    public TextView registerText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hook();
        registerText.setOnClickListener(view -> {
        });
        logButton.setOnClickListener(view -> {
            if (!usernameLog.getText().toString().isEmpty() && !passwordLog.getText().toString().isEmpty()){

            }
        });

    }

    private void hook() {
        usernameLog = findViewById(R.id.usernameLog);
        passwordLog = findViewById(R.id.passwordLog);
        registerText = findViewById(R.id.registerText);
        logButton = findViewById(R.id.register);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if(account == null) return;
        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        accessApp();
                    } else {

                    }
                });
    }
    private void accessApp(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        AppViewModel viewModel = new ViewModelProvider(this).get(AppViewModel.class);

    }
     */
}