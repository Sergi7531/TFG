package com.example.moviez;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

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
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        logButton.setOnClickListener(view -> {
            if (!usernameLog.getText().toString().isEmpty() || !passwordLog.getText().toString().isEmpty()){

            }
        });

    }

    private void hook() {
        usernameLog = findViewById(R.id.usernameLog);
        passwordLog = findViewById(R.id.passwordLog);
        registerText = findViewById(R.id.registerText);
        logButton = findViewById(R.id.logButton);
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
        
    }
}