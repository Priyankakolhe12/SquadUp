package com.example.squadup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private Context context;
    private List<User> teamList;

    public TeamAdapter(Context context, List<User> teamList) {
        this.context = context;
        this.teamList = teamList;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_team_card, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        User user = teamList.get(position);

        holder.txtName.setText(user.getFullName());
        holder.txtRole.setText(user.getRole());

        if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
            Glide.with(context).load(user.getImageUrl()).into(holder.imgProfile);
        } else {
            holder.imgProfile.setImageResource(R.drawable.img_1);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("userId", user.getUid());
            context.startActivity(intent);
        });

        holder.btnMessage.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("receiverUid", user.getUid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public void setTeamList(List<User> teamList) {
        this.teamList = teamList;
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView txtName, txtRole;
        Button btnMessage;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtRole = itemView.findViewById(R.id.txtRole);
            btnMessage = itemView.findViewById(R.id.btnMessage);
        }
    }
}
