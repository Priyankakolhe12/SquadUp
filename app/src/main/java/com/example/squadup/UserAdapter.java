package com.example.squadup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private ArrayList<User> userList;
    private ArrayList<String> invitedUserIds = new ArrayList<>();

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void filterList(ArrayList<User> filteredList) {
        this.userList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.userName.setText(user.getFullName());
        holder.userRole.setText(user.getRole());
        holder.userSkills.setText(user.getSkills() != null ? String.join(", ", user.getSkills()) : "");

        Glide.with(context)
                .load(user.getImageUrl())
                .placeholder(R.drawable.img_1)
                .into(holder.userProfileImage);

        if (invitedUserIds.contains(user.getUid())) {
            holder.inviteButton.setText("Undo Invite");
        } else {
            holder.inviteButton.setText("Invite");
        }

        holder.inviteButton.setOnClickListener(v -> {
            if (invitedUserIds.contains(user.getUid())) {
                undoInvite(user, holder.inviteButton);
            } else {
                sendInvite(user, holder.inviteButton);
            }
        });

        holder.seeDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("userId", user.getUid());
            context.startActivity(intent);
        });
    }

    private void sendInvite(User user, Button inviteButton) {
        String currentUserId = FirebaseAuth.getInstance().getUid();
        String invitedUserId = user.getUid();

        Message message = new Message(
                currentUserId,
                invitedUserId,
                "Hi " + user.getFullName() + ", I'd like to invite you to my team!",
                System.currentTimeMillis()
        );

        FirebaseDatabase.getInstance().getReference()
                .child("Chats")
                .child(currentUserId)
                .child(invitedUserId)
                .push()
                .setValue(message)
                .addOnSuccessListener(aVoid -> {
                    invitedUserIds.add(invitedUserId);
                    inviteButton.setText("Undo Invite");
                    Toast.makeText(context, "Invite Sent! 🚀", Toast.LENGTH_SHORT).show();
                });
    }

    private void undoInvite(User user, Button inviteButton) {
        String currentUserId = FirebaseAuth.getInstance().getUid();
        String invitedUserId = user.getUid();

        FirebaseDatabase.getInstance().getReference()
                .child("Chats")
                .child(currentUserId)
                .child(invitedUserId)
                .removeValue()
                .addOnSuccessListener(aVoid -> {
                    invitedUserIds.remove(invitedUserId);
                    inviteButton.setText("Invite");
                    Toast.makeText(context, "Invite Cancelled ❌", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfileImage;
        TextView userName, userRole, userSkills;
        Button inviteButton, seeDetailsButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            userName = itemView.findViewById(R.id.userName);
            userRole = itemView.findViewById(R.id.userRole);
            userSkills = itemView.findViewById(R.id.userSkills);
            inviteButton = itemView.findViewById(R.id.inviteButton);
            seeDetailsButton = itemView.findViewById(R.id.seeDetailsButton);
        }
    }
}
