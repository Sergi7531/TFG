package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    public BuyTicketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ticket_buy_holder, parent, false);
        return new BuyTicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BuyTicketViewHolder holder, int position) {

        IMDB.api.getMovie(tickets.get(position).filmid, IMDB.apiKey, "es-ES").enqueue(new Callback<Models.Film>() {
            @Override
            public void onResponse(Call<Models.Film> call, Response<Models.Film> response) {
                if (response.body() != null) {
                    Models.Film film = response.body();
                    tickets.get(holder.getAdapterPosition()).filmImage = film.poster_path;
                    tickets.get(holder.getAdapterPosition()).tagline = film.tagline;
                    tickets.get(holder.getAdapterPosition()).filmName = film.title;
                    holder.movieName.setText(film.title);
                    Glide.with(context).load("https://image.tmdb.org/t/p/original" + film.poster_path).into(holder.movieImage);
                }
            }

            @Override
            public void onFailure(Call<Models.Film> call, Throwable t) {
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
        holder.day.setText(day + " " + month + ".");
        holder.time.setText(tickets.get(position).time);
        holder.seat.setText(tickets.get(position).seat+1 + "");
        holder.row.setText(tickets.get(position).row+1 + "");
        holder.room.setText(tickets.get(position).room + "");
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }


    public class BuyTicketViewHolder extends RecyclerView.ViewHolder {

        public ImageView movieImage;
        public TextView movieName;
        public TextView cinemaName;
        public TextView room;
        public TextView row;
        public TextView seat;
        public TextView time;
        public TextView day;

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
        }

    }

}