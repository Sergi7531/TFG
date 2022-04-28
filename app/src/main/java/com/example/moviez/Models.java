package com.example.moviez;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Models {
    public static class Film {
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

        public Film(String title, String poster_path) {
            this.title = title;
            this.poster_path = poster_path;
        }

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
    public class Ticket{
        public String filmName;
        public int line;
        public int seat;
        public int room;
        public LocalDateTime hour;
        public String lang;

        public Ticket(String filmName, int line, int seat, int room, LocalDateTime hour, String lang) {
            this.filmName = filmName;
            this.line = line;
            this.seat = seat;
            this.room = room;
            this.hour = hour;
            this.lang = lang;
        }
    }
    public class User {
        public String userid;
        public String username;
        public String profileImageURL;
        public String email;
        public String password;
        public List<Genre> favoriteGenres;
        public List<Film> watchedFilms;
        public List<Film> favoritedFilms;
        public List<Film> viewLaterFilms;
        public List<User> followers;
        public List<User> following;
        public List<Ticket> tickets;

        public User(String username, String profileImageURL, String email, String password) {
            this.username = username;
            this.profileImageURL = profileImageURL;
            this.email = email;
            this.password = password;
        }
    }
}
