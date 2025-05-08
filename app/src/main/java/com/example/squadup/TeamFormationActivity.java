package com.example.squadup;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamFormationActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String eventId;
    private String teamId;

    private EditText editTeamName, searchEditText;
    private Button btnCreateTeam, btnInvite;
    private ListView teamMembersListView, searchResultsListView;

    private TeamMemberAdapter adapter;
    private ArrayList<User> teamMembers, searchResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_formation);

        editTeamName = findViewById(R.id.editTeamName);
        btnCreateTeam = findViewById(R.id.btnCreateTeam);
        teamMembersListView = findViewById(R.id.teamMembersListView);
        searchEditText = findViewById(R.id.searchEditText);
        searchResultsListView = findViewById(R.id.searchResultsListView);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        teamMembers = new ArrayList<>();
        searchResults = new ArrayList<>();

        adapter = new TeamMemberAdapter(this, teamMembers);
        teamMembersListView.setAdapter(adapter);

        eventId = getIntent().getStringExtra("eventId");
        if (eventId == null) {
            Toast.makeText(this, "Event ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnCreateTeam.setOnClickListener(v -> createTeam());
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnInvite.setOnClickListener(v -> inviteUserToTeam());

        loadTeam();
    }

    private void createTeam() {
        String teamName = editTeamName.getText().toString().trim();
        if (TextUtils.isEmpty(teamName)) {
            Toast.makeText(this, "Please enter team name", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> teamData = new HashMap<>();
        teamData.put("teamName", teamName);
        teamData.put("createdBy", userId);
        teamData.put("members", new ArrayList<String>() {{
            add(userId);
        }});

        db.collection("event_teams")
                .document(eventId)
                .collection("teams")
                .add(teamData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Team created!", Toast.LENGTH_SHORT).show();
                    editTeamName.setText("");
                    loadTeam(); // refresh
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to create team", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadTeam() {
        String userId = auth.getCurrentUser().getUid();

        CollectionReference teamRef = db.collection("event_teams").document(eventId).collection("teams");

        teamRef.get().addOnSuccessListener(querySnapshot -> {
            teamMembers.clear();
            for (QueryDocumentSnapshot doc : querySnapshot) {
                ArrayList<String> members = (ArrayList<String>) doc.get("members");
                if (members != null && members.contains(userId)) {
                    teamId = doc.getId();
                    for (String memberId : members) {
                        db.collection("users").document(memberId)
                                .get()
                                .addOnSuccessListener(userSnap -> {
                                    User user = userSnap.toObject(User.class);
                                    if (user != null) {
                                        user.setUid(userSnap.getId());
                                        teamMembers.add(user);
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    }
                    break;
                }
            }
        });
    }

    private void searchUsers(String query) {
        db.collection("users")
                .whereGreaterThanOrEqualTo("fullName", query)
                .whereLessThanOrEqualTo("fullName", query + "\uf8ff")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    searchResults.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        User user = doc.toObject(User.class);
                        if (user != null && !user.getUid().equals(auth.getCurrentUser().getUid())) {
                            searchResults.add(user);
                        }
                    }
                    // Update search result ListView
                    searchResultsListView.setAdapter(new TeamMemberAdapter(this, searchResults));
                });
    }

    private void inviteUserToTeam() {
        User selectedUser = (User) searchResultsListView.getSelectedItem();
        if (selectedUser == null) {
            Toast.makeText(this, "Select a user to invite", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = selectedUser.getUid();
        Map<String, Object> inviteData = new HashMap<>();
        inviteData.put("userId", userId);
        inviteData.put("status", "pending");

        db.collection("event_teams")
                .document(eventId)
                .collection("teams")
                .document(teamId)
                .collection("invites")
                .add(inviteData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Invite sent to " + selectedUser.getFullName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to send invite", Toast.LENGTH_SHORT).show();
                });
    }
}
