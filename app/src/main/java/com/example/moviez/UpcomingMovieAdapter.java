package com.example.moviez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.UpcomingMovieViewHolder> {
    Context context;
    List<Models.Movie> movies;

    public UpcomingMovieAdapter(List<Models.Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
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
        Picasso.get()
                .load("http://image.tmdb.org/t/p/original" + movies.get(position).poster_path)
                .into(holder.imageFilmHolder);
        System.out.println("http://image.tmdb.org/t/p/original" + movies.get(position).poster_path);
        holder.filmNameHolder.setText(movies.get(position).title);
        holder.releaseDateHolder.setText(movies.get(position).release_date);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class UpcomingMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFilmHolder;
        TextView filmNameHolder;
        TextView releaseDateHolder;

        public UpcomingMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFilmHolder = itemView.findViewById(R.id.imageFilmHolder);
            filmNameHolder = itemView.findViewById(R.id.filmNameHolder);
            releaseDateHolder = itemView.findViewById(R.id.releaseDateFilmHolder);
        }
    }
}
