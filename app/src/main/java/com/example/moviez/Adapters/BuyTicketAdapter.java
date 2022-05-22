package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.IMDB;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyTicketAdapter extends RecyclerView.Adapter<BuyTicketAdapter.BuyTicketViewHolder> {

    Context context;
    List<Models.Ticket> tickets;
    Fragment currentFragment;

    public BuyTicketAdapter(Context context, List<Models.Ticket> tickets, Fragment currentFragment) {
        this.context = context;
        this.tickets = tickets;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public BuyTicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ticket_buy_holder, parent, false);
        return new BuyTicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyTicketViewHolder holder, int position) {

        IMDB.api.getMovie(tickets.get(position).filmid, IMDB.apiKey, "es-ES").enqueue(new Callback<Models.Film>() {
            @Override
            public void onResponse(@NonNull Call<Models.Film> call, @NonNull Response<Models.Film> response) {
                if (response.body() != null) {
                    Models.Film film = response.body();
                    tickets.get(holder.getAdapterPosition()).filmImage = film.poster_path;
                    tickets.get(holder.getAdapterPosition()).tagline = film.tagline;
                    tickets.get(holder.getAdapterPosition()).filmName = film.title;
                    tickets.get(holder.getAdapterPosition()).tagline = film.tagline;
                    holder.movieName.setText(film.title);
                    if (film.title.length() > 22) {
                        holder.movieName.setText(film.title.substring(0, 20) + "...");
                    } else {
                        holder.movieName.setText(film.title);
                    }
                    Glide.with(context).load("https://image.tmdb.org/t/p/original" + film.poster_path).into(holder.movieImage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Models.Film> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        String[] dateSplit = tickets.get(position).date.split("/");

        String day = dateSplit[0];
        String month = dateSplit[1];

        switch (month) {
            case "1":
                month = "Jan";
                break;
            case "2":
                month = "Feb";
                break;
            case "3":
                month = "Mar";
                break;
            case "4":
                month = "Apr";
                break;
            case "5":
                month = "May";
                break;
            case "6":
                month = "Jun";
                break;
            case "7":
                month = "Jul";
                break;
            case "8":
                month = "Aug";
                break;
            case "9":
                month = "Sep";
                break;
            case "10":
                month = "Oct";
                break;
            case "11":
                month = "Nov";
                break;
            case "12":
                month = "Dec";
                break;
        }

        holder.cinemaName.setText(tickets.get(position).cinemaName);
        holder.day.setText(String.format("%s %s.", day, month));
        holder.time.setText(tickets.get(position).time);
        holder.seat.setText(tickets.get(position).seat+1 + "");
        holder.row.setText(tickets.get(position).row+1 + "");
        holder.room.setText(tickets.get(position).room + "");
        if(tickets.get(position).tagline == null) {
            holder.tagline.setVisibility(View.GONE);
        } else {
            holder.tagline.setVisibility(View.VISIBLE);
            holder.tagline.setText(tickets.get(position).tagline);
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public static class BuyTicketViewHolder extends RecyclerView.ViewHolder {

        public ImageView movieImage;
        public TextView movieName;
        public TextView cinemaName;
        public TextView room;
        public TextView row;
        public TextView seat;
        public TextView time;
        public TextView day;
        public TextView tagline;

        public BuyTicketViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movieImageTicket);
            movieName = itemView.findViewById(R.id.ticketMovieName);
            cinemaName = itemView.findViewById(R.id.ticketCinemaName);
            room = itemView.findViewById(R.id.ticketSala);
            row = itemView.findViewById(R.id.ticketFila);
            seat = itemView.findViewById(R.id.ticketAsiento);
            time = itemView.findViewById(R.id.ticketHora);
            day = itemView.findViewById(R.id.ticketDia);
            tagline = itemView.findViewById(R.id.tagline);
        }
    }
}