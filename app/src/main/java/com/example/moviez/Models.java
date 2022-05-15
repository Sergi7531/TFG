package com.example.moviez;

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
        public String title;
        @Nullable public List<Integer> genre_ids;

        public Film(String title, String poster_path) {
            this.title = title;
            this.poster_path = poster_path;
        }

        public Film(int id, String title, String poster_path) {
            this.id = id;
            this.title = title;
            this.poster_path = poster_path;
        }

        public Film() {
        }

        public Film(int id) {
            this.id = id;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public int getRuntime() {
            return runtime;
        }

        public void setRuntime(int runtime) {
            this.runtime = runtime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Nullable
        public List<Integer> getGenre_ids() {
            return genre_ids;
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

        public Room() {}

        public Room(int roomid, String name, String cinemaid, int filmid) {
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

        public Genre(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public Genre(int id, String name, String imageUrl) {
            this.id = id;
            this.name = name;
            this.imageUrl = imageUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Nullable
        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(@Nullable String imageUrl) {
            this.imageUrl = imageUrl;
        }
    }

    public static class Cinema {
        public String cinemaid;
        public String name;
        public String coords;
        public String address;
        public String cinemaImg;
        public String cinemaURL;

//        Empty constructor for Firebase:
        public Cinema() {}

        public Cinema(String cinemaid, String name, String coords, String address, String cinemaImg, String cinemaURL) {
            this.cinemaid = cinemaid;
            this.name = name;
            this.coords = coords;
            this.address = address;
            this.cinemaImg = cinemaImg;
            this.cinemaURL = cinemaURL;
        }

        public String getCinemaid() {
            return cinemaid;
        }

        public void setCinemaid(String cinemaid) {
            this.cinemaid = cinemaid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoords() {
            return coords;
        }

        public void setCoords(String coords) {
            this.coords = coords;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCinemaImg() {
            return cinemaImg;
        }

        public void setCinemaImg(String cinemaImg) {
            this.cinemaImg = cinemaImg;
        }

        public String getCinemaURL() {
            return cinemaURL;
        }

        public void setCinemaURL(String cinemaURL) {
            this.cinemaURL = cinemaURL;
        }
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

//        Empty constructor for Firebase:
        public Ticket() {}

        public Ticket(String ticketid, int filmid, String userid, String filmName, String tagline, String filmImage, String cinemaName, String cinemaCoords, String date, String time, int duration, int row, int seat, int room, boolean valid) {
            this.ticketid = ticketid;
            this.filmid = filmid;
            this.userid = userid;
            this.filmName = filmName;
            this.tagline = tagline;
            this.filmImage = filmImage;
            this.cinemaName = cinemaName;
            this.cinemaCoords = cinemaCoords;
            this.date = date;
            this.time = time;
            this.duration = duration;
            this.row = row;
            this.seat = seat;
            this.room = room;
        }

        @Override
        public String toString() {
            return
                    ticketid +
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
        public List<Integer> favoriteGenres = new ArrayList<>();

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
        }

//      This will be used when the user registers with email and password (e-mail registration):
        public User(String userid, String username, String email, String password, String profileImageURL) {
            this.userid = userid;
            this.username = username;
            this.email = email;
            this.password = password;
            this.profileImageURL = profileImageURL;
            this.favoriteGenres = new ArrayList<>();
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

        public List<Integer> getFavoriteGenres() {
            return favoriteGenres;
        }

        public void setFavoriteGenres(List<Integer> favoriteGenres) {
            this.favoriteGenres = favoriteGenres;
        }
    }

    public class Video {
        public String name;
        public String key;
        public String site;
        public int size;
        public String type;
    }

    public static class Comment {
        public String userid;
        public String comment;
        public String imageUrl;
        public String username;
        public double rating;
        public boolean spoiler;

//        Empty constructor for Firebase:
        public Comment() {
        }

        public Comment(String userid, String comment, String imageUrl, String username, double rating, boolean spoiler) {
            this.userid = userid;
            this.comment = comment;
            this.imageUrl = imageUrl;
            this.username = username;
            this.rating = rating;
            this.spoiler = spoiler;
        }

        public String getComment() {
            return comment;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getUsername() {
            return username;
        }

        public double getRating() {
            return rating;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public boolean isSpoiler() {
            return spoiler;
        }

        public void setSpoiler(boolean spoiler) {
            this.spoiler = spoiler;
        }
    }

    public static class Session {
        public String month;
        public String day;
        public String time;
        public List<String> seats;

        public Session(String month, String day, String time) {
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
        public String id;
        public int row;
        public int column;
        public SeatState state;

        public Seats(String id, int row, int column) {
            this.id = id;
            this.row = row;
            this.column = column;
            this.state = SeatState.FREE;
        }
        public Seats(String id, int row, int column, SeatState state) {
            this.id = id;
            this.row = row;
            this.column = column;
            this.state = state;
        }

    }

    public static class UserActivity {
        public String userImage;
        public String username;
        public String movieImage;
        public int movieId;
        public String movieName;
        public String textToShow;

        public UserActivity(String userImage, String username, int movieId, String movieImage, String movieName, String textToShow) {
            this.userImage = userImage;
            this.username = username;
            this.movieId = movieId;
            this.movieImage = movieImage;
            this.movieName = movieName;
            this.textToShow = textToShow;
        }

        public UserActivity() {

        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getMovieId() {
            return movieId;
        }

        public void setMovieId(int movieId) {
            this.movieId = movieId;
        }

        public String getMovieImage() {
            return movieImage;
        }

        public void setMovieImage(String movieImage) {
            this.movieImage = movieImage;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getTextToShow() {
            return textToShow;
        }

        public void setTextToShow(String textToShow) {
            this.textToShow = textToShow;
        }
    }
}
