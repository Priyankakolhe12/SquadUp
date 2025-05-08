package com.example.squadup;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateTeamActivity extends AppCompatActivity {

    private EditText editTeamName, editEventName, editDescription;
    private Button btnCreateTeam;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        editTeamName = findViewById(R.id.editTeamName);
        editEventName = findViewById(R.id.editEventName);
        editDescription = findViewById(R.id.editDescription);
        btnCreateTeam = findViewById(R.id.btnCreateTeam);

        btnCreateTeam.setOnClickListener(v -> createTeam());
    }

    private void createTeam() {
        String teamName = editTeamName.getText().toString().trim();
        String eventName = editEventName.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String leaderId = auth.getCurrentUser().getUid();

        if (TextUtils.isEmpty(teamName) || TextUtils.isEmpty(eventName) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> team = new HashMap<>();
        team.put("teamName", teamName);
        team.put("eventName", eventName);
        team.put("description", description);
        team.put("leaderId", leaderId);
        team.put("members", new ArrayList<String>()); // Empty members initially

        db.collection("teams")
                .add(team)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Team created successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Close activity after creation
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create team: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
