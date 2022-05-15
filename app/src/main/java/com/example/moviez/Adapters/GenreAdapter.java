package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.moviez.Fragments.PreferencesFragment;
import com.example.moviez.Models;
import com.example.moviez.Models.Genre;
import com.example.moviez.R;

import java.util.List;

public class GenreAdapter extends BaseAdapter {
    Context context;
    List<Genre> genresList;

    public GenreAdapter(Context context, List<Models.Genre> genresList) {
        this.context = context;
        this.genresList = genresList;
    }

    public boolean isSelected(int genreID) {
        for (Integer genreIDInList : PreferencesFragment.selectedGenres) {
            if (genreID == genreIDInList) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getCount() {
        return genresList.size();
    }

    @Override
    public Object getItem(int i) {
        return genresList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View view2 = LayoutInflater.from(context).inflate(R.layout.genre_data, null);

        CardView genreCard;
        TextView titleGenreHolder;
        CardView blueLayer;
        ImageView genre_image;

        genreCard = view2.findViewById(R.id.genreCard);
        titleGenreHolder = view2.findViewById(R.id.titleGenreHolder);
        blueLayer = view2.findViewById(R.id.blueLayer);
        genre_image = view2.findViewById(R.id.genre_image);

        Glide.with(context).load(genresList.get(i).getImageUrl()).centerCrop().into(genre_image);

        titleGenreHolder.setText(genresList.get(i).getName());

        genreCard.setOnClickListener(v -> {

            for (Genre g : genresList) {
                if (g.getName().equals(titleGenreHolder.getText())) {
                    if (!isSelected(g.getId())) {
                        PreferencesFragment.selectedGenres.add(g.getId());
                        blueLayer.setAlpha(1f);
                    } else {
                        blueLayer.setAlpha(0.4f);
                        for (int j = 0; j < PreferencesFragment.selectedGenres.size() - 1; j++) {
                            if (PreferencesFragment.selectedGenres.get(j) == g.getId()) {
                                PreferencesFragment.selectedGenres.remove(j);
                            }
                        }
                    }
                }
            }
        });

        return view2;
    }
}
