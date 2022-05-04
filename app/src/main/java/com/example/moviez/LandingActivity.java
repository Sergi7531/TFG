package com.example.moviez;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LandingActivity extends AppCompatActivity {


    private AnimationFragment animationFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentInit();
        setFragment(animationFragment);

        firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(this));
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if(account == null) {
            Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    setFragment(loginFragment);
                }
            };
            handler.postDelayed(run, 2000);

            return;
        }


        FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        createGoogleAccount();
                    } else {
                        setFragment(loginFragment);
                    }
                });
    }
    private void createGoogleAccount(){
        AppViewModel appViewModel;
        FirebaseFirestore db;
        FirebaseAuth auth;
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        db.collection("users").document(auth.getUid()).addSnapshotListener((snap, exception) -> {
           if(snap.exists()) {
               accessApp();
           } else {
               db.collection("users").document(auth.getUid()).set(new Models.User(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), auth.getCurrentUser().getPhotoUrl().toString(), auth.getCurrentUser().getEmail()));
           }
        });
    }
    private void accessApp(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void fragmentInit() {
        animationFragment = new AnimationFragment();
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();
    }
    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.landingFrame, fragment);
        fragmentTransaction.commit();
    }
}