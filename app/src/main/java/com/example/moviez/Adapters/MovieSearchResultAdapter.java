package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Fragments.HomeFragment;
import com.example.moviez.Fragments.MovieDetailedFragment;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

public class MovieSearchResultAdapter extends RecyclerView.Adapter<MovieSearchResultAdapter.MovieSearchResultViewHolder> {
    Context context;
    List<Models.Film> movies;
    Fragment currentFragment;

    public MovieSearchResultAdapter(Context context, List<Models.Film> movies, Fragment currentFragment) {
        this.context = context;
        this.movies = movies;
        this.currentFragment = currentFragment;
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

        holder.searchHolderLayout.setOnClickListener(view -> {
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


    static class MovieSearchResultViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout searchHolderLayout;
        ImageView imageFilmHolder;
        TextView filmNameHolder;

        public MovieSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            searchHolderLayout = itemView.findViewById(R.id.searchHolderLayout);
            imageFilmHolder = itemView.findViewById(R.id.moviePic);
            filmNameHolder = itemView.findViewById(R.id.usernameSearch);
        }
    }
}