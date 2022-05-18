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
import com.example.moviez.Fragments.PasswordFragment;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.example.moviez.UpdateFilmsInCinemas;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LandingActivity extends AppCompatActivity {

    public static final String PREF_FILE_NAME = "MySharedFile";

    private AnimationFragment animationFragment;
    private LoginFragment loginFragment;

    public AppViewModel appViewModel;

    public FirebaseFirestore db;
    public FirebaseAuth auth;

    private boolean googleLogin;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentInit();
        setFragment(animationFragment);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        googleLogin = true;

        UpdateFilmsInCinemas.initDates();

        firebaseAuthWithGoogle(GoogleSignIn.getLastSignedInAccount(this));
        if (!googleLogin) logWithMail();

    }

    private void logWithMail () {

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String usernameLog = sharedPreferences.getString("userMail", "");
        String passwordLog = sharedPreferences.getString("password", "");
        if (!usernameLog.isEmpty() && !passwordLog.isEmpty()){
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                            usernameLog.trim(),
                            passwordLog
                    ).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    db.collection("users").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).addSnapshotListener((documentSnapshot, e) -> {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("logType", "mail");
                        editor.commit();
                        accessApp();
                    });
                } else {
                    Toast.makeText(this, Objects.requireNonNull(task.getException()).getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Handler handler = new Handler();
            Runnable run = () -> setFragment(loginFragment);
            handler.postDelayed(run, 2000);
        }
    }

    private void firebaseAuthWithGoogle (GoogleSignInAccount account) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(account == null) {
            googleLogin = false;
        }
        else {
            FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(account.getIdToken(), null))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            googleLogin = true;
                            editor.putString("logType", "google");
                            editor.commit();
                            createGoogleAccount();
                        } else {
                            setFragment(loginFragment);
                        }
                    });
        }
    }

    private void createGoogleAccount (){
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        db.collection("users").document(Objects.requireNonNull(auth.getUid())).addSnapshotListener((snap, exception) -> {
            if (snap != null) {
                if(snap.exists()) {
                    editor.putString("logType", "google");
                    editor.commit();
                    accessApp();
                } else {
                    db.collection("users").document(auth.getUid()).set(new Models.User(Objects.requireNonNull(auth.getCurrentUser()).getUid(), auth.getCurrentUser().getDisplayName(), Objects.requireNonNull(auth.getCurrentUser().getPhotoUrl()).toString(), auth.getCurrentUser().getEmail()));
                }
            }
        });
    }

    private void accessApp (){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void fragmentInit () {
        animationFragment = new AnimationFragment();
        loginFragment = new LoginFragment();
    }

    private void setFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.landingFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed () {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.landingFrame);
        if (fragment instanceof PasswordFragment) {
            setFragment(new LoginFragment());
        } else {
            super.onBackPressed();
        }
    }
}