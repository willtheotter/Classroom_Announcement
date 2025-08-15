package com.example.classroomannouncement;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.classroomannouncement.adapters.UserAdapter;
import com.example.classroomannouncement.viewmodels.UserViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.classroomannouncement.dialogs.UserEditDialog;
import androidx.lifecycle.ViewModelProvider;
import com.example.classroomannouncement.Database.Entities.User;

public class ManageUsersActivity extends AppCompatActivity implements UserEditDialog.UserEditListener {
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;
    private static final String EDIT_DIALOG_TAG = "edit_user_dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Setup toolbar
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        // Initialize RecyclerView
        RecyclerView usersRecyclerView = findViewById(R.id.usersRecyclerView);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter();
        usersRecyclerView.setAdapter(userAdapter);

        // Initialize ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getAllUsers().observe(this, users -> {
            if (users != null) {
                userAdapter.setUsers(users);
            }
        });

        // Set click listeners
        userAdapter.setOnItemClickListener(user -> {
            // Open user edit dialog
            UserEditDialog dialog = new UserEditDialog(user, this);
            dialog.show(getSupportFragmentManager(), EDIT_DIALOG_TAG);
        });

        // Add user FAB
        FloatingActionButton addUserButton = findViewById(R.id.addUserButton);
        addUserButton.setOnClickListener(v -> {
            UserEditDialog dialog = new UserEditDialog(null, this);
            dialog.show(getSupportFragmentManager(), EDIT_DIALOG_TAG);
        });
    }

    @Override
    public void onUserEdited(User user) {
        if (user.getId() == 0) {
            // New user (ID not set yet)
            userViewModel.insert(user);
        } else {
            // Existing user
            userViewModel.update(user);
        }
    }
}