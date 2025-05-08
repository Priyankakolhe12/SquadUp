package com.example.squadup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private Context context;
    private ArrayList<Message> messageList;
    private String senderUid;

    public ChatAdapter(Context context, ArrayList<Message> messageList, String senderUid) {
        this.context = context;
        this.messageList = messageList;
        this.senderUid = senderUid;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderUid().equals(senderUid)) {
            return TYPE_SENT;
        } else {
            return TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
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

        if (holder.getItemViewType() == TYPE_SENT) {
            ((SentViewHolder) holder).txtMessage.setText(message.getMessage());
        } else {
            ((ReceivedViewHolder) holder).txtMessage.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
        }
    }
}
