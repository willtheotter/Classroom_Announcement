package com.example.classroomannouncement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classroomannouncement.Database.Entities.User;
import com.example.classroomannouncement.R;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
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

        public UserViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRole = itemView.findViewById(R.id.userRole);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick((User) itemView.getTag());
                }
            });
        }
    }

    public UserAdapter() {
        this.users = new ArrayList<>();
    }

    public void setUsers(List<User> users) {
        if (users != null) {
            this.users = users;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = users.get(position);
        holder.userName.setText(currentUser.getName() != null ? currentUser.getName() : "No Name");
        holder.userEmail.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "No Email");
        holder.userRole.setText(currentUser.isAdmin() ? "Admin" : "User");
        holder.itemView.setTag(currentUser); // Store user object in view tag
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}