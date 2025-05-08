package com.example.squadup;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyTeamsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TeamAdapter teamAdapter;
    private List<Team> teamList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_teams);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerViewTeams);
        progressBar = findViewById(R.id.progressBarTeams);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teamAdapter = new TeamAdapter(this, new ArrayList<>()); // Initialize with empty list
        recyclerView.setAdapter(teamAdapter);

        loadMyTeams();
    }

    private void loadMyTeams() {
        progressBar.setVisibility(View.VISIBLE);

        String currentUserId = auth.getCurrentUser().getUid();

        db.collection("teams")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        teamList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Team team = doc.toObject(Team.class);
                            team.id = doc.getId();

                            // If the current user is part of the team
                            if (team.leaderId.equals(currentUserId) ||
                                    (team.members != null && team.members.contains(currentUserId))) {
                                teamList.add(team);
                            }
                        }

                        // Now extract users from the team members
                        List<User> teamMembers = new ArrayList<>();
                        for (Team team : teamList) {
                            for (String memberId : team.members) {
                                db.collection("users").document(memberId).get()
                                        .addOnCompleteListener(userTask -> {
                                            if (userTask.isSuccessful()) {
                                                User user = userTask.getResult().toObject(User.class);
                                                if (user != null) {
                                                    teamMembers.add(user);
                                                }
                                            }

                                            // Notify adapter after fetching all users
                                            teamAdapter.setTeamList(teamMembers);
                                            teamAdapter.notifyDataSetChanged();
                                        });
                            }
                        }

                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(MyTeamsActivity.this, "Error loading teams", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
