package com.example.moviez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieSearchResultAdapter extends RecyclerView.Adapter<MovieSearchResultAdapter.MovieSearchResultViewHolder> {
    Context context;
    List<Models.Movie> movies;

    public MovieSearchResultAdapter(Context context, List<Models.Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_search_holder, parent, false);
        return new MovieSearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSearchResultViewHolder holder, int position) {
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/original" + movies.get(position).poster_path)
                .centerCrop()
                .into(holder.imageFilmHolder);


        holder.filmNameHolder.setText(movies.get(position).title);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


    class MovieSearchResultViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFilmHolder;
        TextView filmNameHolder;

        public MovieSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFilmHolder = itemView.findViewById(R.id.filmImageSearchHolder);
            filmNameHolder = itemView.findViewById(R.id.filmTitleSearchHolder);
        }
    }
}