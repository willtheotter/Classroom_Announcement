package com.example.classroomannouncement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import com.example.classroomannouncement.adapters.MessageAdapter;
import com.example.classroomannouncement.R;
import com.example.classroomannouncement.viewmodels.ChatViewModel;

public class ChatActivity extends AppCompatActivity {
    private ChatViewModel viewModel;
    private MessageAdapter adapter;
    private String currentUserId;
    private String currentUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get current user info (you'll need to pass this from login)
        currentUserId = "user@example.com"; // Replace with actual user ID
        currentUserName = "Current User";   // Replace with actual user name

        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(currentUserId);
        recyclerView.setAdapter(adapter);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.getUserMessages(currentUserId).observe(this, messages -> {
            adapter.submitList(messages);
            recyclerView.scrollToPosition(messages.size() - 1);
            viewModel.markMessagesAsRead(currentUserId);
        });

        // Setup send button
        EditText etMessage = findViewById(R.id.etMessage);
        Button btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                viewModel.sendMessage(message, currentUserId, currentUserName);
                etMessage.setText("");
            }
        });
    }
}