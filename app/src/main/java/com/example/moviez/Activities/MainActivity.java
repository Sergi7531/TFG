package com.example.moviez.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviez.Fragments.BuyTicketFragment;
import com.example.moviez.Fragments.HomeFragment;
import com.example.moviez.Fragments.MoviesFragment;
import com.example.moviez.Fragments.ProfileFragment;
import com.example.moviez.Fragments.TicketsFragment;
import com.example.moviez.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav_bottom;
    private FloatingActionButton buyMovie;
    private FrameLayout mainFrame;
    AppViewModel appViewModel;

//    Fragments:
        private HomeFragment homeFragment;
        private MoviesFragment moviesFragment;
        private TicketsFragment ticketsFragment;
        private ProfileFragment profileFragment;
        private BuyTicketFragment buyTicketFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav_bottom = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        homeFragment = new HomeFragment();
        moviesFragment = new MoviesFragment();
        ticketsFragment = new TicketsFragment();
        profileFragment = new ProfileFragment();
        buyTicketFragment = new BuyTicketFragment();

        buyMovie = findViewById(R.id.buyMovie);

        setFragment(homeFragment);

//        We need to set the default fragment to the home fragment:

//        Iterate the mapa variable entries:

        nav_bottom.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    //open the HomeFragment
//                    get user favoriteGenres:
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
                    profileFragment.userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    setFragment(profileFragment);
                    return true;
                default:
                    return false;
            }
        });

        buyMovie.setOnClickListener(v -> {
            setFragment(buyTicketFragment);
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}