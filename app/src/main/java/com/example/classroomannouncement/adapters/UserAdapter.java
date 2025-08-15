package com.example.classroomannouncement.adapters;  // Make sure this matches your actual package name

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.R;  // Make sure this import matches your app's package

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView userEmail;
        public TextView userRole;

        public UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRole = itemView.findViewById(R.id.userRole);
        }
    }

    public UserAdapter() {
        this.users = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User currentUser = users.get(position);
        holder.userName.setText(currentUser.getName());
        holder.userEmail.setText(currentUser.getEmail());
        holder.userRole.setText(currentUser.isAdmin() ? "Admin" : "User");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(currentUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}