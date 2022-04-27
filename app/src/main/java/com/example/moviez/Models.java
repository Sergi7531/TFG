package com.example.moviez;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Models {
    class Movie {
        public boolean adult;
        public String backdrop_path;
        public int budget;
        public ArrayList<Genre> genres;
        public String homepage;
        public int id;
        public String imdb_id;
        public String original_language;
        public String original_title;
        public String overview;
//        We will order films by popularity, so we will use popularity instead of vote_average
        public double popularity;
        public String poster_path;
        public String release_date;
        public int revenue;
        public int runtime;
        //        public ArrayList<SpokenLanguage> spoken_languages;
        public String status;
        public String tagline;
        public String title;
        public boolean video;
        public double vote_average;
        public int vote_count;
    }

    public class Genre {
        public int id;
        public String name;
        public @Nullable String imageUrl;

        public Genre(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Genre(int id, String name, String imageUrl) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
        }
    }
}
