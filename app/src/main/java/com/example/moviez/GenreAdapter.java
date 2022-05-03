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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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

        holder.genreCard.setOnClickListener(v -> {

            for(Genre g : genresList) {
                for(Integer genreIDInList : PreferencesFragment.selectedGenres) {
                    if(g.getId() == genreIDInList) {
                        System.out.println("Removing genre: " + g.getName());
                        PreferencesFragment.selectedGenres.remove(genreIDInList);
                        holder.blueBackground.setAlpha(0f);
                    } else {
                        System.out.println("Adding genre: " + g.getName());
                        PreferencesFragment.selectedGenres.add(g.getId());
                        holder.blueBackground.setAlpha(0.1f);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        public CardView genreCard;
        public TextView titleGenreHolder;
        public View blueBackground;
        public ImageView genre_image;

        public GenreViewHolder(@NonNull View itemView) {
            super(itemView);
            this.genreCard = itemView.findViewById(R.id.genreCard);
            this.titleGenreHolder = itemView.findViewById(R.id.titleGenreHolder);
            this.blueBackground = itemView.findViewById(R.id.blueBackground);
            this.genre_image = itemView.findViewById(R.id.genre_image);
        }
    }
}
