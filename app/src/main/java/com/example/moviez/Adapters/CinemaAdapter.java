package com.example.moviez.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Fragments.HomeFragment;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

public class CinemaAdapter extends RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder> {
    public Context context;
    public List<Models.Cinema> cinemaList;
    public Fragment currentFragment;


    public CinemaAdapter(List<Models.Cinema> cinemas, Context context, HomeFragment currentFragment) {
        this.cinemaList = cinemas;
        this.context = context;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public CinemaAdapter.CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cinema_data, parent, false);
        return new CinemaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaAdapter.CinemaViewHolder holder, int position) {

//        Can we do a new CinemaDetailedFragment??
        holder.cinemaHolder.setOnClickListener(view -> {
//            CinemaDetailedFragment cinemaDetailedFragment = new CinemaDetailedFragment(cinemas.get(position).cinemaid);
//            setFragment(movieDetailedFragment);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(cinemaList.get(position).cinemaURL));
            context.startActivity(intent);

        });
        Glide.with(context).load(cinemaList.get(position).cinemaImg).into(holder.imageCinemaHolder);

        holder.nameCinemaHolder.setText(cinemaList.get(position).name);

    }

    @Override
    public int getItemCount() {
        return cinemaList.size();
    }

    private void setFragment(Fragment fragment) {
        currentFragment
                .getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .addToBackStack(HomeFragment.class.getSimpleName())
                .commit();
    }

    public class CinemaViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout cinemaHolder;
        public ImageView imageCinemaHolder;
        public TextView nameCinemaHolder;

        public CinemaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cinemaHolder = itemView.findViewById(R.id.cinemaHolder);
            this.imageCinemaHolder = itemView.findViewById(R.id.imageCinemaHolder);
            this.nameCinemaHolder = itemView.findViewById(R.id.nameCinemaHolder);
        }
    }
}
