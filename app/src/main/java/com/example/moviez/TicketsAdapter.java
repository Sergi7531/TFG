package com.example.moviez;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        holder.filmNameTicketDetail.setText(ticket.filmName);
        holder.taglineTicket.setText(ticket.tagline);
        holder.cinemaNameTicketDetail.setText(ticket.cinemaName);
        holder.dayTicketDetail.setText(ticket.date);
        holder.timeTicketDetail.setText(ticket.time);
        holder.hourTicketDetail.setText(String.valueOf(ticket.duration));
        holder.roomTicketDetail.setText(ticket.room + "");
        holder.rowTicketDetail.setText(ticket.row + "");
        holder.seatTicketDetail.setText(ticket.seat + "");

        String ticketInfo = (ticket.filmName + ";;" + ticket.tagline + ";;" + ticket.cinemaName +
                ";;" + ticket.date + ";;" + ticket.time + ";;" + ticket.room + ";;"
                + ticket.row + "" + ";;" + ticket.seat);

        try {
            passInfoToQR(holder, ticketInfo);
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
        public TextView hourTicketDetail;
        public TextView timeTicketDetail;
        public TextView durationTicket;
        public TextView roomTicketDetail;
        public TextView rowTicketDetail;
        public TextView seatTicketDetail;
        public ImageView qrCode;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTicketDetail = itemView.findViewById(R.id.imageTicketDetail);
            filmNameTicketDetail = itemView.findViewById(R.id.filmNameTicketDetail);
            taglineTicket = itemView.findViewById(R.id.taglineTicket);
            hourTicketDetail = itemView.findViewById(R.id.hourTicketDetail);
            cinemaNameTicketDetail = itemView.findViewById(R.id.cinemaNameTicketDetail);
            dayTicketDetail = itemView.findViewById(R.id.dayTicketDetail);
            timeTicketDetail = itemView.findViewById(R.id.durationTicket);
            durationTicket = itemView.findViewById(R.id.durationTicket);
            roomTicketDetail = itemView.findViewById(R.id.roomTicketDetail);
            rowTicketDetail = itemView.findViewById(R.id.rowTicketDetail);
            seatTicketDetail = itemView.findViewById(R.id.seatTicketDetail);
            qrCode = itemView.findViewById(R.id.qrCode);
        }
    }

    private void passInfoToQR(@NonNull TicketViewHolder holder, String ticketInfo) throws WriterException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(ticketInfo, BarcodeFormat.QR_CODE, 120, 120);
        Bitmap bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.RGB_565);

        for (int x = 0; x < 120; x++) {
            for (int y = 0; y < 120; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        holder.qrCode.setImageBitmap(bitmap);
    }
}