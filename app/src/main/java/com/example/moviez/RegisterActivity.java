package com.example.moviez;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private ImageView profilePic;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        email = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        profilePic = findViewById(R.id.profilePic);
        register = findViewById(R.id.register);

        final ActivityResultLauncher<String> phoneGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if(uri != null) {
                //appViewModel.setUriImagenSeleccionada(uri);
            }
        });

        profilePic.setOnClickListener(v -> {
            phoneGallery.launch("image/*");
        });
    }
}