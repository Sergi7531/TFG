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
                if  (g.getName().equals(holder.titleGenreHolder.getText())) {
                    if (!isSelected(g.getId())) {
                        PreferencesFragment.selectedGenres.add(g.getId());
                    } else {
                        PreferencesFragment.selectedGenres.remove(g.getId());
                    }
                }
            }
        });

//        We need to update the user favorite genres in the firebase database:


    }

//    Create isSelected method. This method will be used to check if the genre is selected or not.
//    Iterate over PreferencesFragment.selectedGenres and check if the genre is in the list.

    public boolean isSelected(int genreID) {
        for(Integer genreIDInList : PreferencesFragment.selectedGenres) {
            if(genreID == genreIDInList) {
                return true;
            }
        }
        return false;
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
