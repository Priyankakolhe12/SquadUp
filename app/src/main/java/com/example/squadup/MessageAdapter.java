package com.example.squadup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Message> messageList;
    private String currentUserId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderUid().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).txtMessage.setText(message.getMessage());
        } else if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).txtMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessageSent);
        }
    }

    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessageReceived);
        }
    }
}
