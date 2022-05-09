package com.example.moviez.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    List<Models.Comment> comments;
    Context context;

    public CommentAdapter(List<Models.Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_holder, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Models.Comment comment = comments.get(position);

        if(comment.getImageUrl() != null && !comment.getImageUrl().isEmpty()) {
            Glide.with(context).load(comment.getImageUrl()).into(holder.profileImage);
        } else {
            holder.profileImage.setImageResource(R.drawable.ic_baseline_person_24);
        }

        holder.username.setText(comment.getUsername());
        holder.spoilerImage.setAlpha(1f);

        if(comment.spoiler) {
            holder.comment.setText("Este comentario contiene spoilers.\nPulsa el icono para verlo.");
            holder.rating.setText("-");
            holder.spoilerImage.setImageResource(R.drawable.ic_baseline_warning_amber_24);
        } else {
            holder.comment.setText(comment.getComment());
            holder.rating.setText(String.valueOf(comment.getRating()));
        }
        holder.spoilerImage.setOnClickListener(view -> {
            holder.spoilerImage.setAlpha(0f);
            holder.comment.setText(comment.comment);
            holder.rating.setText(String.valueOf(comment.getRating()));
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView comment;
        public TextView username;
        public ImageView profileImage;
        public TextView rating;

        public ImageView spoilerImage;

        public CommentViewHolder(android.view.View itemView) {
            super(itemView);
            comment = itemView.findViewById(R.id.commentUserHolder);
            username = itemView.findViewById(R.id.usernameMyUserHolder);
            profileImage = itemView.findViewById(R.id.profilePicUserHolder);
            rating = itemView.findViewById(R.id.userRatingHolder);
            spoilerImage = itemView.findViewById(R.id.spoilerImage);
        }

    }


}
