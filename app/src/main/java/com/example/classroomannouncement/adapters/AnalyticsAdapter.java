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

    public AnalyticsAdapter() {
    }

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
        holder.eventTypeText.setText(analytics.getEventType());
        holder.detailsText.setText(analytics.getDetails());
        holder.timestampText.setText(dateFormat.format(analytics.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return analyticsList.size();
    }

    static class AnalyticsViewHolder extends RecyclerView.ViewHolder {
        TextView eventTypeText;
        TextView detailsText;
        TextView timestampText;

        public AnalyticsViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTypeText = itemView.findViewById(R.id.eventTypeText);
            detailsText = itemView.findViewById(R.id.detailsText);
            timestampText = itemView.findViewById(R.id.timestampText);
        }
    }
}