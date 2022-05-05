package com.example.moviez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    List<Models.User> users;

    public UserAdapter(List<Models.User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_holder, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        if (users.get(position).profileImageURL == null) {
            Glide.with(context)
                    .load(R.drawable.ic_baseline_person_24)
                    .centerCrop()
                    .into(holder.userImageHolder);
        } else {
            Glide.with(context)
                    .load(users.get(position).getProfileImageURL())
                    .centerCrop()
                    .into(holder.userImageHolder);
        }

        holder.usernameHolder.setText(users.get(position).username);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageHolder;
        TextView usernameHolder;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageHolder = itemView.findViewById(R.id.userImageHolder);
            usernameHolder = itemView.findViewById(R.id.usernameHolder);
        }
    }
}
