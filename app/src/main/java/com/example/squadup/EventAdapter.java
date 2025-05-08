package com.example.squadup;

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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private EventClickListener listener;

    public interface EventClickListener {
        void onEventClick(Event event);
    }

    public EventAdapter(Context context, List<Event> eventList, EventClickListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.txtTitle.setText(event.getTitle() != null ? event.getTitle() : "Untitled Event");
        holder.txtCategory.setText(event.getMode() != null ? event.getMode() : "Online/Offline");
        holder.txtDate.setText(event.getDate() != null ? "Date: " + event.getDate() : "Date: TBA");

        // Load poster image if available, otherwise use placeholder
        if (event.getPosterUrl() != null && !event.getPosterUrl().isEmpty()) {
            Glide.with(context).load(event.getPosterUrl()).into(holder.imgEvent);
        } else {
            holder.imgEvent.setImageResource(R.drawable.ic_event); // fallback icon
        }

        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEvent;
        TextView txtTitle, txtCategory, txtDate;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEvent = itemView.findViewById(R.id.imgEvent);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtDate = itemView.findViewById(R.id.txtDate);
        }
    }
}
