package com.example.squadup;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class TeamInvitesActivity extends AppCompatActivity implements InviteAdapter.InviteActionListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private InviteAdapter inviteAdapter;
    private List<Invite> inviteList = new ArrayList<>();
    private ListenerRegistration inviteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_invites);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewInvites);
        progressBar = findViewById(R.id.progressBarInvites);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        inviteAdapter = new InviteAdapter(this, inviteList, this);
        recyclerView.setAdapter(inviteAdapter);

        listenForInvites();
    }

    private void listenForInvites() {
        progressBar.setVisibility(View.VISIBLE);

        String currentUserId = auth.getCurrentUser().getUid();

        inviteListener = db.collection("invites")
                .whereEqualTo("receiverId", currentUserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(TeamInvitesActivity.this, "Error loading invites", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        inviteList.clear();
                        for (QueryDocumentSnapshot doc : snapshots) {
                            Invite invite = doc.toObject(Invite.class);
                            invite.setInviteId(doc.getId());
                            inviteList.add(invite);
                        }
                        inviteAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onAcceptInvite(Invite invite) {
        db.collection("invites").document(invite.getInviteId())
                .update("status", "accepted")
                .addOnSuccessListener(unused -> Toast.makeText(this, "Invite accepted!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error accepting invite", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRejectInvite(Invite invite) {
        db.collection("invites").document(invite.getInviteId())
                .update("status", "rejected")
                .addOnSuccessListener(unused -> Toast.makeText(this, "Invite rejected!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Error rejecting invite", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inviteListener != null) {
            inviteListener.remove();
        }
    }
}
