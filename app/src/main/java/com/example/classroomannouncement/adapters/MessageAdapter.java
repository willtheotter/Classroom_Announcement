package com.example.classroomannouncement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomannouncement.Database.Entities.Message;
import com.example.classroomannouncement.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends ListAdapter<Message, MessageAdapter.MessageViewHolder> {
    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.content.equals(newItem.content) &&
                    oldItem.isRead == newItem.isRead;
        }
    };

    private String currentUserId;

    public MessageAdapter(String currentUserId) {
        super(DIFF_CALLBACK);
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0) { // Sent message
            view = inflater.inflate(R.layout.item_message_sent, parent, false);
        } else { // Received message
            view = inflater.inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).senderId.equals(currentUserId) ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = getItem(position);
        holder.bind(message);
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView contentText, timeText, senderText;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            contentText = itemView.findViewById(R.id.message_content);
            timeText = itemView.findViewById(R.id.message_time);
            senderText = itemView.findViewById(R.id.message_sender);
        }

        public void bind(Message message) {
            contentText.setText(message.content);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            timeText.setText(sdf.format(new Date(message.timestamp)));

            if (senderText != null) { // Only in received messages
                senderText.setText(message.senderName);
            }
        }
    }
}