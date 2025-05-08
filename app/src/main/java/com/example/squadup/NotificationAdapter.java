package com.example.squadup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private ArrayList<NotificationModel> notificationList;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_card, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notification = notificationList.get(position);

        holder.txtTitle.setText(notification.getTitle());
        holder.txtBody.setText(notification.getBody());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault());
        holder.txtTime.setText(sdf.format(new Date(notification.getTimestamp())));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtBody, txtTime;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtBody = itemView.findViewById(R.id.txtBody);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
