package com.example.classroomannouncement.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.classroomannouncement.Database.Entities.Analytics;
import com.example.classroomannouncement.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.AnalyticsViewHolder> {
    private List<Analytics> analyticsList = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public void setAnalyticsList(List<Analytics> analyticsList) {
        this.analyticsList = analyticsList != null ? analyticsList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnalyticsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_analytics, parent, false);
        return new AnalyticsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalyticsViewHolder holder, int position) {
        Analytics analytics = analyticsList.get(position);

        // Set event type with emoji
        holder.eventType.setText(formatEventType(analytics.getEventType()));

        // Set view count
        holder.viewCount.setText(String.format(Locale.getDefault(),
                "Views: %d", analytics.getViewCount()));

        // Set product info if available
        if (analytics.getProductId() != null) {
            String productText = analytics.getProductName() != null ?
                    analytics.getProductName() : "Product #" + analytics.getProductId();
            holder.productInfo.setText(productText);
            holder.productInfo.setVisibility(View.VISIBLE);
        } else {
            holder.productInfo.setVisibility(View.GONE);
        }

        // Set timestamp
        holder.timestamp.setText(dateFormat.format(analytics.getTimestamp()));

        // Set user ID if available
        if (analytics.getUserId() != null) {
            holder.userId.setText("User: " + analytics.getUserId());
            holder.userId.setVisibility(View.VISIBLE);
        } else {
            holder.userId.setVisibility(View.GONE);
        }
    }

    private String formatEventType(String eventType) {
        if (eventType == null) return "Unknown Event";

        switch (eventType.toLowerCase()) {
            case "view": return "👁️ View";
            case "click": return "🖱️ Click";
            case "purchase": return "💰 Purchase";
            default: return eventType;
        }
    }

    @Override
    public int getItemCount() {
        return analyticsList.size();
    }

    static class AnalyticsViewHolder extends RecyclerView.ViewHolder {
        TextView eventType;
        TextView viewCount;
        TextView productInfo;
        TextView timestamp;
        TextView userId;

        public AnalyticsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventType = itemView.findViewById(R.id.eventType);
            viewCount = itemView.findViewById(R.id.viewCount);
            productInfo = itemView.findViewById(R.id.productInfo);
            timestamp = itemView.findViewById(R.id.timestamp);
            userId = itemView.findViewById(R.id.userId);
        }
    }
}