package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private boolean isScrollEnabled = true;
    List<Models.Seats> seats = new ArrayList<>();
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
        holder.seatImage.setOnClickListener(view -> {
            // Select new Seat
            if (seat.state.equals(Models.SeatState.FREE)) {
                holder.seatImage.setImageResource(R.drawable.ic_rectangle_selected);
                //TODO
                // Save position to be saved
            } else if (seat.state.equals(Models.SeatState.SELECTED)){
                holder.seatImage.setImageResource(R.drawable.ic_rectangle_void);
                //TODO
                //Delete position
            }
        });

    }
    @Override
    public int getItemCount() {
        return seats.size();
    }

    public class SeatViewHolder extends RecyclerView.ViewHolder {
        public ImageView seatImage;
        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatImage = itemView.findViewById(R.id.seatImage);
        }
    }
}
