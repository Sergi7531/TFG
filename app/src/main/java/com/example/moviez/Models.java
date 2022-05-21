package com.example.moviez;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Models {

    public static class Film {

        public String backdrop_path;
        public int id;
        public String poster_path;
        public String release_date;
        public int runtime;
        public String overview;
        public String title;
        public String tagline;
        @Nullable public List<Integer> genre_ids;

        public Film (String title, String poster_path) {
            this.title = title;
            this.poster_path = poster_path;
        }

        public Film (int id, String title, String poster_path) {
            this.id = id;
            this.title = title;
            this.poster_path = poster_path;
        }

        public Film (int id, String poster_path, int runtime, String overview, String title) {
            this.id = id;
            this.poster_path = poster_path;
            this.runtime = runtime;
            this.overview = overview;
            this.title = title;
        }

        public Film () {
        }

        public Film (int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setGenre_ids(@Nullable List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }
    }

    public static class Room {

        public int roomid;
        public String name;
        public String cinemaid;
        public int filmid;

        public Room () {}

        public Room (int roomid, String name, String cinemaid, int filmid) {
            this.roomid = roomid;
            this.name = name;
            this.cinemaid = cinemaid;
            this.filmid = filmid;
        }
    }


    public static class Genre {

        public int id;
        public String name;
        public @Nullable String imageUrl;

        public Genre () {}

        public Genre (int id, String name, @Nullable String imageUrl) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public int getId () {
            return id;
        }

        public void setId (int id) {
            this.id = id;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        @Nullable
        public String getImageUrl () {
            return imageUrl;
        }

    }

    public static class Cinema {

        public String cinemaid;
        public String name;
        public String coords;
        public String address;
        public String cinemaImg;
        public String cinemaURL;

        public Cinema () {}

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public void setAddress (String address) { this.address = address; }

    }

    public static class Ticket {

        public String ticketid;
        public int filmid;
        public String userid;
        public String filmName;
        public String tagline;
        public String filmImage;
        public String cinemaName;
        public String cinemaCoords;
        public String date;
        public String time;
        public int duration;
        public int row;
        public int seat;
        public int room;

        public Ticket() {}

        @NonNull
        @Override
        public String toString() {
            return ticketid +
                    ";;" + filmid +
                    ";;" + userid +
                    ";;" + filmName +
                    ";;" + tagline +
                    ";;" + filmImage +
                    ";;" + cinemaName +
                    ";;" + cinemaCoords +
                    ";;" + date +
                    ";;" + time +
                    ";;" + duration +
                    ";;" + row +
                    ";;" + seat +
                    ";;" + room;
        }
    }

    public static class User {

        public String userid;
        public String username;
        public String profileImageURL;
        public String email;
        public String password;
        public List<Integer> favoriteGenres;

        public User () {}

        public User (String userid, String username, String profileImageURL, String email) {
            this.userid = userid;
            this.username = username;
            this.profileImageURL = profileImageURL;
            this.email = email;
            this.favoriteGenres = new ArrayList<>();
        }

        public User (String userid, String username, String email, String password, String profileImageURL) {
            this.userid = userid;
            this.username = username;
            this.email = email;
            this.password = password;
            this.profileImageURL = profileImageURL;
            this.favoriteGenres = new ArrayList<>();
        }

        public String getUserid () {
            return userid;
        }

        public void setUserid (String userid) {
            this.userid = userid;
        }

        public String getUsername () {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<Integer> getFavoriteGenres() {
            return favoriteGenres;
        }

    }

    public static class Video {
        public String name;
        public String key;
        public String site;
        public int size;
        public String type;

        public Video() {}

        public Video(String name, String site, int size, String type) {
            this.name = name;
            this.site = site;
            this.size = size;
            this.type = type;
        }
    }

    public static class Comment {

        public String userid;
        public String comment;
        public String imageUrl;
        public String username;
        public double rating;
        public boolean spoiler;

        public Comment () {}

        public Comment (String userid, String comment, String imageUrl, String username, double rating, boolean spoiler) {
            this.userid = userid;
            this.comment = comment;
            this.imageUrl = imageUrl;
            this.username = username;
            this.rating = rating;
            this.spoiler = spoiler;
        }

        public String getComment () {
            return comment;
        }

        public String getImageUrl () {
            return imageUrl;
        }

        public String getUsername () {
            return username;
        }

        public double getRating () {
            return rating;
        }

        public void setUsername (String username) {
            this.username = username;
        }

        public String getUserid () {
            return userid;
        }

        public void setUserid (String userid) {
            this.userid = userid;
        }

    }

    public static class Session {

        public String sessionid;
        public String month;
        public String day;
        public String time;
        public List<Integer> seats;

        public Session () {}

        public Session (String sessionid, String month, String day, String time) {
            this.sessionid = sessionid;
            this.month = month;
            this.day = day;
            this.time = time;
            this.seats = new ArrayList<>();
        }
    }

    public enum SeatState {
        BUSY,
        FREE,
        SELECTED
    }

    public static class Seats {

        public int row;
        public int seat;
        public SeatState state;

        public Seats () {}

        public Seats (int row, int seat, SeatState state) {
            this.row = row;
            this.seat = seat;
            this.state = state;
        }
    }

    public static class UserActivity {

        public String userImage;
        public String username;
        public String movieImage;
        public int movieId;
        public String userId;
        public String movieName;
        public String textToShow;

        public UserActivity() { }

        public String getUserImage () {
            return userImage;
        }

        public String getUsername () {
            return username;
        }

        public void setUsername (String username) {
            this.username = username;
        }

        public int getMovieId () {
            return movieId;
        }

        public String getMovieImage () {
            return movieImage;
        }

        public String getTextToShow () {
            return textToShow;
        }
    }
}
