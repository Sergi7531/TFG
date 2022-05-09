package com.example.moviez.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.moviez.Activities.AppViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public abstract class AppFragment extends Fragment {
    public static AppViewModel appViewModel;
    public FirebaseFirestore db;
    public FirebaseAuth auth;
    public MutableLiveData<Uri> uriImagenSeleccionada = new MutableLiveData<>();

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }
}
