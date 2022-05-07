package com.example.moviez;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class TicketsAdapter extends PagerAdapter {
    private List<Models.Ticket> mTickets;
    public Context context;

    public TicketsAdapter(List<Models.Ticket> tickets, Context context) {
        mTickets = tickets;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mTickets.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return false;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }


}