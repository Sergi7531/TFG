package com.example.moviez.Adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Models;
import com.example.moviez.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder> {

    private final List<Models.Ticket> tickets;
    public Context context;
    public boolean qrShown = false;
    ObjectAnimator objectAnimator;

    public TicketsAdapter(List<Models.Ticket> tickets, Context context) {
        this.tickets = tickets;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ticket_data, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Models.Ticket ticket = tickets.get(position);

        Glide.with(context).load("https://image.tmdb.org/t/p/original" + ticket.filmImage).into(holder.imageTicketDetail);

        holder.qrButton.setOnClickListener(view -> {
            if (holder.qrCard.getAlpha() == 0.0){
                qrShown = true;
                objectAnimator = ObjectAnimator.ofFloat(holder.qrCard, "alpha", 0f, 1f).setDuration(300);
                objectAnimator.start();
                holder.qrButton.setText(R.string.qr_hide);
            } else {
                qrShown = false;
                holder.qrButton.setText(R.string.qr_show);
                objectAnimator = ObjectAnimator.ofFloat(holder.qrCard, "alpha", 1f, 0f).setDuration(300);
                objectAnimator.start();
            }

        });

        holder.linearCinema.setOnClickListener(v -> {
            String[] cinemaCoordsSplit = ticket.cinemaCoords.split(",");

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo: " + ticket.cinemaCoords + "?q=" + "(" + ticket.cinemaName + ")"));
            context.startActivity(intent);
        });

//        if filmname is too long, cut it:
        if (ticket.filmName.length() > 22){
            holder.filmNameTicketDetail.setText(ticket.filmName.substring(0, 20) + "...");
        } else {
            holder.filmNameTicketDetail.setText(ticket.filmName);
        }
        holder.taglineTicket.setText(ticket.tagline);
        holder.cinemaNameTicketDetail.setText(ticket.cinemaName);
        holder.dayTicketDetail.setText(ticket.date);
        holder.timeTicketDetail.setText(ticket.time);

        holder.roomTicketDetail.setText(ticket.room + "");
        holder.rowTicketDetail.setText(ticket.row+1 + "");
        holder.seatTicketDetail.setText(ticket.seat+1 + "");

        try {
            passInfoToQR(holder, ticket.toString());
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageTicketDetail;
        public TextView filmNameTicketDetail;
        public TextView taglineTicket;
        public TextView cinemaNameTicketDetail;
        public TextView dayTicketDetail;
        public TextView timeTicketDetail;
        public TextView roomTicketDetail;
        public TextView rowTicketDetail;
        public TextView seatTicketDetail;
        public ImageView qrCode;
        public TextView linearCinema;
        public TextView qrButton;
        public CardView qrCard;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTicketDetail = itemView.findViewById(R.id.imageTicketDetail);
            filmNameTicketDetail = itemView.findViewById(R.id.filmNameTicketDetail);
            taglineTicket = itemView.findViewById(R.id.taglineTicket);
            cinemaNameTicketDetail = itemView.findViewById(R.id.linearCinema);
            dayTicketDetail = itemView.findViewById(R.id.dayTicketDetail);
            timeTicketDetail = itemView.findViewById(R.id.timeTicketDetail);
            roomTicketDetail = itemView.findViewById(R.id.roomTicketDetail);
            rowTicketDetail = itemView.findViewById(R.id.rowTicketDetail);
            seatTicketDetail = itemView.findViewById(R.id.seatTicketDetail);
            qrCode = itemView.findViewById(R.id.qrCode);
            linearCinema = itemView.findViewById(R.id.linearCinema);
            qrButton = itemView.findViewById(R.id.qrButton);
            qrCard = itemView.findViewById(R.id.qrCard);
        }
    }

    private void passInfoToQR(@NonNull TicketViewHolder holder, String ticketInfo) throws WriterException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(ticketInfo, BarcodeFormat.QR_CODE, 300, 300);
        Bitmap bitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);

        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        holder.qrCode.setImageBitmap(bitmap);
    }
}