package com.example.moviez.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviez.Fragments.AnimationFragment;
import com.example.moviez.Fragments.LoginFragment;
import com.example.moviez.Fragments.RegisterFragment;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LandingActivity extends AppCompatActivity {

    public static final String PREF_FILE_NAME = "MySharedFile";
    private AnimationFragment animationFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    public AppViewModel appViewModel;
    public FirebaseFirestore db;
    public FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentInit();
        setFragment(animationFragment);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        logWithMail();
        firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(this));
    }

    private void logWithMail() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String usernameLog = sharedPreferences.getString("userMail", "");
        String passwordLog = sharedPreferences.getString("password", "");
        if (!usernameLog.isEmpty() && !passwordLog.isEmpty()){
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                            usernameLog,
                            passwordLog
                    ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    db.collection("users").document(auth.getCurrentUser().getUid()).addSnapshotListener((documentSnapshot, e) -> {
                        if (documentSnapshot.toObject(Models.User.class).favoriteGenres.isEmpty() || documentSnapshot.toObject(Models.User.class).favoriteGenres == null) {
                            accessApp();
                        }
                    });
                } else {
                    Toast.makeText(this, task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
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