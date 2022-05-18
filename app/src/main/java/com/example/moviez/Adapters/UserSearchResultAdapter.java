package com.example.moviez.Adapters;

import static com.example.moviez.Activities.AppViewModel.auth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviez.Fragments.ProfileFragment;
import com.example.moviez.Models;
import com.example.moviez.R;

import java.util.List;
import java.util.Objects;

public class UserSearchResultAdapter extends RecyclerView.Adapter<UserSearchResultAdapter.UserSearchResultViewHolder>{

    Context context;
    List<Models.User> users;
    Fragment currentFragment;

    public UserSearchResultAdapter(Context context, List<Models.User> users, Fragment currentFragment) {
        this.context = context;
        this.users = users;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public UserSearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_search_holder, parent, false);
        return new UserSearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchResultViewHolder holder, int position) {

        Models.User user = users.get(position);
           if (!user.getUserid().equals(Objects.requireNonNull(auth.getCurrentUser()).getUid())) {
                holder.usernameSearch.setText(user.username);

               if (!user.profileImageURL.equals("")) {
                   Glide.with(context)
                           .load(user.profileImageURL)
                           .centerCrop()
                           .into(holder.userProfilePic);
               }

                holder.searchHolderLayout.setOnClickListener(view -> {
                    ProfileFragment profileFragment = new ProfileFragment(user.userid);
                    setFragment(profileFragment);
                });
            }
           else {
               if (!user.profileImageURL.equals("")) {
                   Glide.with(context)
                           .load(user.profileImageURL)
                           .centerCrop()
                           .into(holder.userProfilePic);
               }
               holder.usernameSearch.setText(user.username);
               holder.searchHolderLayout.setOnClickListener(view -> Toast.makeText(view.getContext(), "¿Te estás buscando a tí mismo?", Toast.LENGTH_SHORT).show());
           }
    }

    private void setFragment(Fragment fragment) {
        currentFragment
                .getFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public static class UserSearchResultViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout searchHolderLayout;
        ImageView userProfilePic;
        TextView usernameSearch;

        public UserSearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            searchHolderLayout = itemView.findViewById(R.id.searchHolderLayout);
            userProfilePic = itemView.findViewById(R.id.userProfilePic);
            usernameSearch = itemView.findViewById(R.id.usernameSearch);
        }
    }
}