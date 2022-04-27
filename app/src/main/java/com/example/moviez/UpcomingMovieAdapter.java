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

public class UpcomingMovieAdapter extends RecyclerView.Adapter<UpcomingMovieAdapter.UpcomingMovieViewHolder> {
    Context context;
    List<Models.Movie> movies;
    byte type;

    public UpcomingMovieAdapter(List<Models.Movie> movies, Context context, byte type) {
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

//        Adapter is going to be reused, so this is why we use this boolean:

//        type == 0 -> Movies Coming Soon (show date)
//        type == 1 -> Movies Now Playing
//        type == 2 -> Movies recommendations (show year)

        if(type == 0) {
            holder.releaseDateHolder.setText(movies.get(position).release_date);
            holder.releaseDateHolder.setAlpha(1f);
        }

        if(type == 2) {
            holder.yearFilmHolder.setText(movies.get(position).release_date.split("-")[0]);
            holder.yearFilmHolder.setAlpha(1f);
        }




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


    class UpcomingMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFilmHolder;
        TextView filmNameHolder;
        TextView releaseDateHolder;
        TextView yearFilmHolder;

        public UpcomingMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFilmHolder = itemView.findViewById(R.id.imageFilmHolder);
            filmNameHolder = itemView.findViewById(R.id.filmNameHolder);
            releaseDateHolder = itemView.findViewById(R.id.releaseDateFilmHolder);
            yearFilmHolder = itemView.findViewById(R.id.yearFilmHolder);
        }
    }
}
