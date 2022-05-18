package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Fragments.HomeFragment;
import com.example.moviez.Fragments.MovieDetailedFragment;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    public Context context;
    public List<Models.Film> movies;
    public Fragment currentFragment;

    public FilmAdapter(List<Models.Film> movies, Context context, Fragment currentFragment) {
        this.movies = movies;
        this.context = context;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_holder, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/original" + movies.get(position).poster_path)
                .centerCrop()
                .into(holder.imageFilmHolder);

        holder.imageFilmHolder.setOnClickListener(view -> {
            MovieDetailedFragment movieDetailedFragment = new MovieDetailedFragment(movies.get(position).id);
            setFragment(movieDetailedFragment);
        });
    }

    private void setFragment(Fragment fragment) {
        if (currentFragment
                .getFragmentManager() != null) {
            currentFragment
                    .getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .addToBackStack(HomeFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFilmHolder;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFilmHolder = itemView.findViewById(R.id.imageFilmHolder);
        }
    }
}
