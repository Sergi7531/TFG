package com.example.moviez;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.example.moviez.Models.User;
import com.example.moviez.AppFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText username;
    private TextInputEditText mail;
    private TextInputEditText password;
    private TextInputEditText confirmPassword;
    private ImageView profilePic;
    private Button register;
    private AppViewModel appViewModel;
    private Uri uriProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        mail = findViewById(R.id.mail);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm);
        profilePic = findViewById(R.id.profilePic);
        register = findViewById(R.id.register);
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        final ActivityResultLauncher<String> phoneGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if(uri != null) {
                appViewModel.setUriImagenSeleccionada(uri);
                System.out.println(uri);
            }
        });

        profilePic.setOnClickListener(v -> {
            phoneGallery.launch("image/*");
        });

        appViewModel.uriImagenSeleccionada.observe(this, uri -> {
            if(uri!=null) {
                uriProfilePic = uri;
                Glide.with(this).load(uriProfilePic).into(profilePic);
            } else {
                uriProfilePic = null;
                profilePic.setImageResource(R.drawable.ic_baseline_person_24);
            }
        });

        register.setOnClickListener(view1 -> {
//            Check for empty input:
            String name = username.getText().toString();
            String email = mail.getText().toString();
            String pass = password.getText().toString();
            if (name.isEmpty()) {
                username.setError("This field must not be empty!");
                return;
            }
            if (email.isEmpty()) {
                mail.setError("This field must not be empty!");
                return;
            }
            if (pass.isEmpty()) {
                password.setError("This field must not be empty!");
                return;
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if(uriProfilePic != null) {
                        FirebaseStorage.getInstance().getReference("/profileimgs/"+ UUID.randomUUID()+".jpg")
                                .putFile(uriProfilePic)
                                .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                                .addOnSuccessListener(imageUrl-> saveUser(imageUrl));
                    } else {
                        saveUser(null);
                    }

                    Intent intent = new Intent(this, LoginActivity.class);
                } else {
                    Toast.makeText(this, task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void saveUser(Uri imageUri) {
        User userToAdd = new User();
        String imageUrl;

        if(imageUri == null) {
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/apifirebase-f6f9e.appspot.com/o/profileimgs%2Fic_baseline_person_24.xml?alt=media&token=896e0cc4-b4af-4800-9a9b-a21b8cac7a0d";
        } else {
            imageUrl = imageUri.toString();
        }

        userToAdd.profileImageURL = imageUrl;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        userToAdd.userid = firebaseAuth.getCurrentUser().getUid();
        userToAdd.username = username.getText().toString();
        userToAdd.email = mail.getText().toString();
        userToAdd.password = password.getText().toString();
        userToAdd.favoritedFilms = new ArrayList<>();
        userToAdd.favoriteGenres = new ArrayList<>();
        userToAdd.watchedFilms = new ArrayList<>();
        userToAdd.viewLaterFilms = new ArrayList<>();
        userToAdd.followers = new ArrayList<>();
        userToAdd.following = new ArrayList<>();
        userToAdd.tickets = new ArrayList<>();

        db.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .set(userToAdd);

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username.getText().toString())
                .setPhotoUri(imageUri)
                .build();
        firebaseAuth.getCurrentUser().updateProfile(profileUpdates);

        appViewModel.userlogged = userToAdd;
    }
}