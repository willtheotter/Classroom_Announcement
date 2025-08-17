package com.example.classroomannouncement;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;
import com.example.classroomannouncement.adapters.UserAdapter;
import com.example.classroomannouncement.viewmodels.UserViewModel;
import com.example.classroomannouncement.Database.Entities.User;

public class ViewUsersActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_users);

        // Initialize RecyclerView
        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter();
        usersRecyclerView.setAdapter(userAdapter);

        // Set click listener
        userAdapter.setOnItemClickListener(user -> {
            if (user != null) {
                showUserDetails(user);
            } else {
                Toast.makeText(this, "User data not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null && !users.isEmpty()) {
                userAdapter.setUsers(users);
            } else {
                Toast.makeText(this, "No users found in database", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showUserDetails(User user) {
        String details = String.format(
                "Name: %s\nEmail: %s\nRole: %s",
                user.getName(),
                user.getEmail(),
                user.isAdmin() ? "Admin" : "Regular User"
        );
        Toast.makeText(this, details, Toast.LENGTH_LONG).show();
    }
}