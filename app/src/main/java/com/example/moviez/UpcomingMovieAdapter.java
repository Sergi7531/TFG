package com.example.moviez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.UpcomingMovieViewHolder> {
    Context context;
    List<Models.Film> movies;
    byte type;

    public UpcomingMovieAdapter(List<Models.Film> movies, Context context, byte type) {
        this.movies = movies;
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public UpcomingMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_holder, parent, false);
        return new UpcomingMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingMovieViewHolder holder, int position) {
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/original" + movies.get(position).poster_path)
                .centerCrop()
                .into(holder.imageFilmHolder);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class UpcomingMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFilmHolder;

        public UpcomingMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFilmHolder = itemView.findViewById(R.id.imageFilmHolder);
        }
    }
}
