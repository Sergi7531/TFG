package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

public class UserActivityAdapter extends RecyclerView.Adapter<UserActivityAdapter.UserActivityViewHolder> {

    private List<Models.UserActivity> userActivities;
    public Context context;

    public UserActivityAdapter(List<Models.UserActivity> userActivities, Context context) {
        this.userActivities = userActivities;
        this.context = context;
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

        if(userActivity.getUserImage() != null && !userActivity.getUserImage().isEmpty()) {
            Glide.with(context).load(userActivity.getUserImage()).into(holder.userImage);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_baseline_person_24);
        }

        Glide.with(context).load(userActivity.getMovieImage()).into(holder.movieImage);

        holder.dayText.setText(userActivity.getDayText());
        holder.movieName.setText(userActivity.getMovieImage());
        holder.hourText.setText(userActivity.getHourText());
    }

    @Override
    public int getItemCount() {
        return userActivities.size();
    }

    public class UserActivityViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImage;
        public ImageView movieImage;
        public TextView movieName;
        public TextView dayText;
        public TextView hourText;

        public UserActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            movieImage = itemView.findViewById(R.id.movieImage);
            movieName = itemView.findViewById(R.id.movieName);
            dayText = itemView.findViewById(R.id.dayText);
            hourText = itemView.findViewById(R.id.hourText);
        }
    }
}
