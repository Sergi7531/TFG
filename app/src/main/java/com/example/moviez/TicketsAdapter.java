package com.example.moviez;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder> {
    private List<Models.Ticket> tickets;
    public Context context;

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

        holder.linearCinema.setOnClickListener(v -> {
//      Intent to google maps with the cinema location (cinemaCoords):
//        Split cinemaCoords into latitude and longitude:
            String[] cinemaCoordsSplit = ticket.cinemaCoords.split(",");


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo: " + ticket.cinemaCoords + "?q=" + "(" + ticket.cinemaName + ")"));
            context.startActivity(intent);
        });

        //Implementar campo 'valid' para tickets:



        holder.filmNameTicketDetail.setText(ticket.filmName);
        holder.taglineTicket.setText(ticket.tagline);
        holder.cinemaNameTicketDetail.setText(ticket.cinemaName);
        holder.dayTicketDetail.setText(ticket.date);
        holder.timeTicketDetail.setText(ticket.time);

//        Convert duration to hours and minutes:
        int duration = ticket.duration;
        int hours = duration / 60;
        int minutes = duration % 60;

        String durationString = hours + "h " + minutes + "m";
        holder.durationTicket.setText(durationString);
        holder.roomTicketDetail.setText(ticket.room + "");
        holder.rowTicketDetail.setText(ticket.row + "");
        holder.seatTicketDetail.setText(ticket.seat + "");

        try {
            passInfoToQR(holder, ticket.toString());
        } catch (WriterException e) {
            e.printStackTrace();
        }

        String[] time = ticket.time.split(":");
        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);
        holder.durationTicket.setText(hour + ":" + minute);

    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class TicketViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageTicketDetail;
        public TextView filmNameTicketDetail;
        public TextView taglineTicket;
        public TextView cinemaNameTicketDetail;
        public TextView dayTicketDetail;
        public TextView timeTicketDetail;
        public TextView durationTicket;
        public TextView roomTicketDetail;
        public TextView rowTicketDetail;
        public TextView seatTicketDetail;
        public ImageView qrCode;
        public LinearLayout linearCinema;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTicketDetail = itemView.findViewById(R.id.imageTicketDetail);
            filmNameTicketDetail = itemView.findViewById(R.id.filmNameTicketDetail);
            taglineTicket = itemView.findViewById(R.id.taglineTicket);
            cinemaNameTicketDetail = itemView.findViewById(R.id.cinemaNameTicketDetail);
            dayTicketDetail = itemView.findViewById(R.id.dayTicketDetail);
            timeTicketDetail = itemView.findViewById(R.id.timeTicketDetail);
            durationTicket = itemView.findViewById(R.id.durationTicket);
            roomTicketDetail = itemView.findViewById(R.id.roomTicketDetail);
            rowTicketDetail = itemView.findViewById(R.id.rowTicketDetail);
            seatTicketDetail = itemView.findViewById(R.id.seatTicketDetail);
            qrCode = itemView.findViewById(R.id.qrCode);
            linearCinema = itemView.findViewById(R.id.linearCinema);
        }
    }

    private void passInfoToQR(@NonNull TicketViewHolder holder, String ticketInfo) throws WriterException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(ticketInfo, BarcodeFormat.QR_CODE, 200, 200);
        Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

        for (int x = 0; x < 200; x++) {
            for (int y = 0; y < 200; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        holder.qrCode.setImageBitmap(bitmap);
    }

}