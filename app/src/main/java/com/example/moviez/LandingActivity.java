package com.example.moviez;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
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
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                setFragment(loginFragment);
            }
        };
        handler.postDelayed(r, 2000);
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