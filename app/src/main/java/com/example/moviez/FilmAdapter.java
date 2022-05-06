package com.example.moviez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {
    Context context;
    List<Models.Film> movies;
    Fragment currentFragment;

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
//            Bundle bundle = new Bundle();
//            bundle.putInt("filmId", movies.get(position).id);
//            movieDetailedFragment.setArguments(bundle);
            setFragment(movieDetailedFragment);

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
        return movies.size();
    }


    class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFilmHolder;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFilmHolder = itemView.findViewById(R.id.imageFilmHolder);
        }
    }
}
