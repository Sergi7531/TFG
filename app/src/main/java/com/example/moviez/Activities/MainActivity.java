package com.example.moviez.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.moviez.Fragments.BuyTicketFragment;
import com.example.moviez.Fragments.ChangeUsernameFragment;
import com.example.moviez.Fragments.EditProfileFragment;
import com.example.moviez.Fragments.HomeFragment;
import com.example.moviez.Fragments.MovieDetailedFragment;
import com.example.moviez.Fragments.MoviesFragment;
import com.example.moviez.Fragments.NewCommentFragment;
import com.example.moviez.Fragments.PasswordFragment;
import com.example.moviez.Fragments.ProfileFragment;
import com.example.moviez.Fragments.QRScanFragment;
import com.example.moviez.Fragments.TicketsFragment;
import com.example.moviez.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_FILE_NAME = "MySharedFile";
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
        private EditProfileFragment editProfileFragment;
        private MovieDetailedFragment movieDetailedFragment;

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
        editProfileFragment = new EditProfileFragment();
        movieDetailedFragment = new MovieDetailedFragment();

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
            buyTicketFragment = new BuyTicketFragment(0, R.id.main_frame);
            setFragment(buyTicketFragment);
        });
    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_frame);
        SharedPreferences sharedPreferences = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (fragment instanceof MoviesFragment || fragment instanceof TicketsFragment || fragment instanceof ProfileFragment) {
            setFragment(homeFragment);
            nav_bottom.setSelectedItemId(R.id.home);
        }
        else if (fragment instanceof QRScanFragment) {
            setFragment(ticketsFragment);
            nav_bottom.setSelectedItemId(R.id.tickets);
        }
        else if (fragment instanceof EditProfileFragment) {
            setFragment(profileFragment);
        }
        else if (fragment instanceof ChangeUsernameFragment) {
            setFragment(editProfileFragment);
        }
        else if (fragment instanceof PasswordFragment) {
            setFragment(editProfileFragment);
        }
        else if (fragment instanceof MovieDetailedFragment) {
            int filmId = appViewModel.currentFilmId;
            if (filmId == 0){
                setFragment(homeFragment);
                nav_bottom.setSelectedItemId(R.id.home);
            } else {
                MovieDetailedFragment movieDetailedFragment2 = new MovieDetailedFragment(filmId);
                setFragment(movieDetailedFragment2);
                appViewModel.currentFilmId = 0;
            }
        }
        else if (fragment instanceof NewCommentFragment) {
            setFragment(movieDetailedFragment);
        }

        /*
        if (nav_bottom.getSelectedItemId() == R.id.home) {
            super.onBackPressed();
            finish();
        }
        else {
            nav_bottom.setSelectedItemId(R.id.home);
        }
        */
    }

    private void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.addToBackStack("tag");
        fragmentTransaction.commit();
    }
}