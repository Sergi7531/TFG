package com.example.moviez;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static class User {
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

//        Empty constructor for Firebase:
        public User() {
        }



//        This constructor will be for Google sign-in and will be used when the user registers with Google:
        public User(String userid, String username, String profileImageURL, String email) {
            this.userid = userid;
            this.username = username;
            this.profileImageURL = profileImageURL;
            this.email = email;
            this.favoriteGenres = new ArrayList<>();
            this.watchedFilms = new ArrayList<>();
            this.favoritedFilms = new ArrayList<>();
            this.viewLaterFilms = new ArrayList<>();
            this.followers = new ArrayList<>();
            this.following = new ArrayList<>();
            this.tickets = new ArrayList<>();
        }

//      This will be used when the user registers with email and password (e-mail registration):
        public User(String userid, String username, String email, String password, String profileImageURL) {
            this.userid = userid;
            this.username = username;
            this.email = email;
            this.password = password;
            this.profileImageURL = profileImageURL;
            this.favoriteGenres = new ArrayList<>();
            this.watchedFilms = new ArrayList<>();
            this.favoritedFilms = new ArrayList<>();
            this.viewLaterFilms = new ArrayList<>();
            this.followers = new ArrayList<>();
            this.following = new ArrayList<>();
            this.tickets = new ArrayList<>();
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getProfileImageURL() {
            return profileImageURL;
        }

        public void setProfileImageURL(String profileImageURL) {
            this.profileImageURL = profileImageURL;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public List<Genre> getFavoriteGenres() {
            return favoriteGenres;
        }

        public void setFavoriteGenres(List<Genre> favoriteGenres) {
            this.favoriteGenres = favoriteGenres;
        }

        public List<Film> getWatchedFilms() {
            return watchedFilms;
        }

        public void setWatchedFilms(List<Film> watchedFilms) {
            this.watchedFilms = watchedFilms;
        }

        public List<Film> getFavoritedFilms() {
            return favoritedFilms;
        }

        public void setFavoritedFilms(List<Film> favoritedFilms) {
            this.favoritedFilms = favoritedFilms;
        }

        public List<Film> getViewLaterFilms() {
            return viewLaterFilms;
        }

        public void setViewLaterFilms(List<Film> viewLaterFilms) {
            this.viewLaterFilms = viewLaterFilms;
        }

        public List<User> getFollowers() {
            return followers;
        }

        public void setFollowers(List<User> followers) {
            this.followers = followers;
        }

        public List<User> getFollowing() {
            return following;
        }

        public void setFollowing(List<User> following) {
            this.following = following;
        }

        public List<Ticket> getTickets() {
            return tickets;
        }

        public void setTickets(List<Ticket> tickets) {
            this.tickets = tickets;
        }
    }
}
