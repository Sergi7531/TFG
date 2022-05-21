package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Fragments.SeatsFragment;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    List<Models.Seats> seats;
    public Context context;

    public SeatAdapter(List<Models.Seats> seats, Context context) {
        this.seats = seats;
        this.context = context;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.seat_holder, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Models.Seats seat = seats.get(position);

        if (seat.state.equals(Models.SeatState.BUSY)) {
            holder.seatImage.setImageResource(R.drawable.ic_rectangle_taken);
            disableSeat(seat, holder);
        } else if (seat.state.equals(Models.SeatState.FREE)) {
            holder.seatImage.setImageResource(R.drawable.ic_rectangle_void);
            enableSeat(seat, holder);
        } else if (seat.state.equals(Models.SeatState.SELECTED)) {
            holder.seatImage.setImageResource(R.drawable.ic_rectangle_selected);
            selectSeat(seat);
        }

        holder.seatImage.setOnClickListener(view -> {
            if (seat.state.equals(Models.SeatState.FREE)) {
                SeatsFragment.selectedSeats++;
                holder.seatImage.setImageResource(R.drawable.ic_rectangle_selected);
                selectSeat(seat);
            } else if (seat.state.equals(Models.SeatState.SELECTED)) {
                SeatsFragment.selectedSeats--;
                holder.seatImage.setImageResource(R.drawable.ic_rectangle_void);
                enableSeat(seat, holder);
            }
        });

    }

    private void disableSeat(Models.Seats seat, @NonNull SeatViewHolder holder) {
        seat.state = Models.SeatState.BUSY;
        holder.seatImage.setEnabled(false);
        holder.seatImage.setClickable(false);
    }

    private void enableSeat(Models.Seats seat, @NonNull SeatViewHolder holder) {
        seat.state = Models.SeatState.FREE;
        holder.seatImage.setEnabled(true);
        holder.seatImage.setClickable(true);
    }

    private void selectSeat(Models.Seats seat) {
        seat.state = Models.SeatState.SELECTED;
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        public ImageView seatImage;
        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatImage = itemView.findViewById(R.id.seatImage);
        }
    }
}