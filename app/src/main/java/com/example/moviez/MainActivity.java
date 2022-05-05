package com.example.moviez;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav_bottom;
    private FrameLayout mainFrame;

//    Fragments:
        private HomeFragment homeFragment;
        private MoviesFragment moviesFragment;
        private TicketsFragment ticketsFragment;
        private ProfileFragment profileFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav_bottom = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        homeFragment = new HomeFragment();
        moviesFragment = new MoviesFragment();
        ticketsFragment = new TicketsFragment();
        profileFragment = new ProfileFragment();

        setFragment(homeFragment);

//        We need to set the default fragme        setFragment(homeFragment);nt to the home fragment:

//        Iterate the mapa variable entries:

        nav_bottom.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    //open the HomeFragment
                    setFragment(homeFragment);
                    return true;
                case R.id.movies:
                    //open the MoviesFragment
                    setFragment(moviesFragment);
                    return true;
                case R.id.tickets:
                    //open the TicketsFragment
                    setFragment(ticketsFragment);
                    return true;
                case R.id.profile:
                    //open the ProfileFragment
                    setFragment(profileFragment);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}