package com.example.squadup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteViewHolder> {

    private Context context;
    private List<Invite> inviteList;
    private FirebaseFirestore db;
    private InviteActionListener inviteActionListener;

    public interface InviteActionListener {
        void onAcceptInvite(Invite invite);
        void onRejectInvite(Invite invite);
    }

    public InviteAdapter(Context context, List<Invite> inviteList, InviteActionListener listener) {
        this.context = context;
        this.inviteList = inviteList;
        this.inviteActionListener = listener;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public InviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invite_card, parent, false);
        return new InviteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteViewHolder holder, int position) {
        Invite invite = inviteList.get(position);

        holder.txtMessage.setText(invite.getMessage());

        holder.btnAccept.setOnClickListener(v -> inviteActionListener.onAcceptInvite(invite));
        holder.btnReject.setOnClickListener(v -> inviteActionListener.onRejectInvite(invite));
    }

    @Override
    public int getItemCount() {
        return inviteList.size();
    }

    static class InviteViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        Button btnAccept, btnReject;

        public InviteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
