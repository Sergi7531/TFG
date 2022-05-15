package com.example.moviez.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviez.Activities.LandingActivity;
import com.example.moviez.Adapters.CommentAdapter;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends AppFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String PREF_FILE_NAME = "MySharedFile";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardView profileName;
    public CardView passwordName;
    public CardView theme;
    public CardView editPicture;
    public CardView credits;
    public Button closeSession;
    public ImageView goBackButton;
    public TextView textViewSergio;
    public CardView creditsCard;
    public ImageView creditButton;
    public ImageView darkModeToggle;

    private MutableLiveData<Uri> uriProfilePic = new MutableLiveData<>();

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        hook(view);

        profileName.setOnClickListener(v -> {
            setFragment(new ChangeUsernameFragment());
        });

        passwordName.setOnClickListener(v -> {
            setFragment(new PasswordFragment());
        });


        closeSession.setOnClickListener(v -> {
            GoogleSignInClient googleSignInAccount = GoogleSignIn.getClient(requireContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build());
            editor.putString("userMail", "");
            editor.putString("password", "");
            editor.putBoolean("autoLogGoogle", false);
            editor.commit();
            auth.signOut();
            googleSignInAccount.signOut();
            Intent intent = new Intent();
            intent.setClass(getActivity(), LandingActivity.class);
            getActivity().startActivity(intent);


        });

        int isNightMode = getContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        if (isNightMode == Configuration.UI_MODE_NIGHT_YES){
            darkModeToggle.setImageResource(R.drawable.ic_toggle_on);
        } else {
            darkModeToggle.setImageResource(R.drawable.ic_toggle_off);
        }
        theme.setOnClickListener(v -> {
            switch (isNightMode) {
                case Configuration.UI_MODE_NIGHT_YES:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
        });

        credits.setOnClickListener(v -> {
            if (creditsCard.getAlpha() == 0f){
                creditButton.setRotation(90);
                creditsCard.setAlpha(1f);
            } else {
                creditButton.setRotation(0);
                creditsCard.setAlpha(0f);
            }
        });

        textViewSergio.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Créditos a GitHub Copilot =)", Toast.LENGTH_SHORT).show();
        });
//        textViewRaul.setOnClickListener(v -> {
//            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
//        });
//        textViewJordi.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jplanasmartinez"));
//            startActivity(intent);
//        });

        goBackButton.setOnClickListener(v -> {
            setFragment(new ProfileFragment());
        });

        final ActivityResultLauncher<String> phoneGallery = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                uriProfilePic.postValue(uri);
                appViewModel.setUriImagenSeleccionada(uri);
            } else {
                uriProfilePic = null;
            }
        });

        editPicture.setOnClickListener(v -> {
            phoneGallery.launch("image/*");
            uriProfilePic.observe(getViewLifecycleOwner(), uri -> {
                if (uri != null) {
                    FirebaseStorage.getInstance().getReference("/profileimgs/" + UUID.randomUUID() + ".jpg")
                            .putFile(uri)
                            .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                            .addOnSuccessListener(imageUrl -> saveUser(auth.getCurrentUser().getUid(), imageUrl));
                }
            });
        });




    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void saveUser(String userid, Uri imageUri) {

        String imageUrl = "";

        if (imageUri != null && !imageUri.toString().equals("")) {
            imageUrl = imageUri.toString();
        }

        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .update("profileImageURL", imageUrl);

//        Update all user comments with the new image url:
        String finalImageUrl = imageUrl;


        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(imageUrl))
                .build();
        auth.getCurrentUser().updateProfile(profileUpdates);

//        Get all documents in the collection:
//
        db.collection("comments")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    db.collection("comments").document(document.getId()).collection("comments").get().addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            Models.Comment comment = document.toObject(Models.Comment.class);
                            if (comment.userid.equals(userid)) {
                                db.collection("comments").document(document.getId()).collection("comments").document(comment.userid).update("imageUrl", finalImageUrl);
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    public void hook(View view) {
        profileName = view.findViewById(R.id.changeName);
        passwordName = view.findViewById(R.id.changePassword);
        theme = view.findViewById(R.id.changeDarkLightTheme);
        credits = view.findViewById(R.id.credits);
        editPicture = view.findViewById(R.id.changePicture);
        closeSession = view.findViewById(R.id.closeSession);
        goBackButton = view.findViewById(R.id.goBackButton);
        textViewSergio = view.findViewById(R.id.textViewSergio);
        creditsCard = view.findViewById(R.id.creditsCard);
        creditButton = view.findViewById(R.id.creditButton);
        darkModeToggle = view.findViewById(R.id.darkModeToggle);
    }

    private void setFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }
}