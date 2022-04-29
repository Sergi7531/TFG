package com.example.moviez;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Predicate;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {
    Context context;
    List<Genre> genresList;

    public GenreAdapter(Context context, List<Genre> genresList) {
        this.context = context;
        this.genresList = genresList;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.genre_data, parent, false);
        return new GenreViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.titleGenreHolder.setText(genresList.get(position).getName());

        holder.constraintLayout.setOnClickListener(v -> {

            Predicate<Genre> p1 = g -> g.getId() == genresList.get(position).getId();

            if( genresList.stream().anyMatch(p1)) {
                PreferencesFragment.selectedGenres.remove(genresList.get(position).getId());
            } else {
                PreferencesFragment.selectedGenres.add(genresList.get(position).getId());
            }

            PreferencesFragment.selectedGenres.forEach(System.out::println);
        });




    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public class GenreViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public ImageView imageTriangle;
        public ImageView image_backgroundHolder;
        public TextView titleGenreHolder;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            this.constraintLayout = itemView.findViewById(R.id.genreHolder);
            this.imageTriangle = itemView.findViewById(R.id.imageTriangle);
            this.image_backgroundHolder = itemView.findViewById(R.id.image_backgroundHolder);
            this.titleGenreHolder = itemView.findViewById(R.id.titleGenreHolder);
        }
    }
}
