package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Fragments.HomeFragment;
import com.example.moviez.Fragments.MovieDetailedFragment;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.UserActivityViewHolder> {

    private List<Models.UserActivity> userActivities;
    public Context context;
    public Fragment currentFragment;

    public UserActivityAdapter(List<Models.UserActivity> userActivities, Context context, Fragment currentFragment) {
        this.userActivities = userActivities;
        this.context = context;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public UserActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_activity_holder, parent, false);
        return new UserActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserActivityAdapter.UserActivityViewHolder holder, int position) {
        Models.UserActivity userActivity = userActivities.get(position);

        holder.titleUserActivity.setText("Novedad de "+userActivity.username);

        if(userActivity.getUserImage() != null && !userActivity.getUserImage().isEmpty()) {
            Glide.with(context).load(userActivity.getUserImage()).into(holder.userImage);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_baseline_person_24);
        }

        Glide.with(context).load("https://image.tmdb.org/t/p/original" + userActivity.getMovieImage()).into(holder.movieImage);

        holder.dayText.setText(userActivity.getDayText());
        holder.textToShow.setText(userActivity.getTextToShow());
        holder.hourText.setText(userActivity.getHourText());

        holder.userActivityCard.setOnClickListener(view -> {
            if(userActivity.movieId != 0) {
                MovieDetailedFragment movieDetailedFragment = new MovieDetailedFragment(userActivity.getMovieId());
                setFragment(movieDetailedFragment);
            }
        });
    }

    private void setFragment(Fragment fragment) {
        currentFragment
                .getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(HomeFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public int getItemCount() {
        return userActivities.size();
    }

    public class UserActivityViewHolder extends RecyclerView.ViewHolder {

        public CardView userActivityCard;
        public ImageView userImage;
        public ImageView movieImage;
        public TextView titleUserActivity;
        public TextView textToShow;
        public TextView dayText;
        public TextView hourText;

        public UserActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            userActivityCard = itemView.findViewById(R.id.userActivityCard);
            userImage = itemView.findViewById(R.id.userImage);
            movieImage = itemView.findViewById(R.id.movieImage);
            titleUserActivity = itemView.findViewById(R.id.titleUserActivity);
            textToShow = itemView.findViewById(R.id.textToShow);
            dayText = itemView.findViewById(R.id.dayText);
            hourText = itemView.findViewById(R.id.hourText);
        }
    }
}
