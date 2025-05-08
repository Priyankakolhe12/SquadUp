package com.example.squadup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailActivity extends AppCompatActivity {

    private ImageView eventImage, backButton;
    private TextView eventTitle, eventDescription, eventDate, eventMode, eventSkills, eventLink, eventCollege, eventLastDate, eventNotes;
    private Button joinButton;

    private DatabaseReference joinedEventsRef;
    private FirebaseAuth auth;
    private String eventId;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        eventImage = findViewById(R.id.eventImage);
        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        eventDate = findViewById(R.id.eventDate);
        eventMode = findViewById(R.id.eventMode);
        eventSkills = findViewById(R.id.eventSkills);
        eventLink = findViewById(R.id.eventLink);
        eventCollege = findViewById(R.id.eventCollege);
        eventLastDate = findViewById(R.id.eventLastDate);
        eventNotes = findViewById(R.id.eventNotes);
        backButton = findViewById(R.id.backButton);
        joinButton = findViewById(R.id.joinButton); // 👉 IMPORTANT: Initialize joinButton here

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getUid();

        backButton.setOnClickListener(v -> finish());

        eventId = getIntent().getStringExtra("eventId");

        if (eventId != null) {
            loadEventDetails(eventId);
        }

        joinButton.setOnClickListener(v -> joinEvent());
    }

    private void loadEventDetails(String eventId) {
        FirebaseDatabase.getInstance().getReference("Events")
                .child(eventId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Event event = snapshot.getValue(Event.class);
                            if (event != null) {
                                eventTitle.setText(event.getTitle());
                                eventDescription.setText(event.getDescription());
                                eventDate.setText("Date: " + event.getDate());
                                eventMode.setText("Mode: " + event.getMode());
                                eventSkills.setText("Skills: " + String.join(", ", event.getSkillsRequired()));
                                eventLink.setText("Link: " + event.getEventLink());
                                eventCollege.setText("College/Platform: " + event.getPlatform());
                                eventLastDate.setText("Last Date: " + event.getLastDateToRegister());
                                eventNotes.setText("Notes: " + event.getNotes());

                                Glide.with(EventDetailActivity.this)
                                        .load(event.getPosterUrl())
                                        .placeholder(R.drawable.img_1)
                                        .into(eventImage);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void joinEvent() {
        if (currentUserId == null || eventId == null) return;

        joinedEventsRef = FirebaseDatabase.getInstance().getReference("JoinedEvents")
                .child(eventId)
                .child(currentUserId);

        joinedEventsRef.setValue(true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EventDetailActivity.this, "Joined successfully! 🎯", Toast.LENGTH_SHORT).show();
                    joinButton.setEnabled(false);
                    joinButton.setText("Joined");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EventDetailActivity.this, "Failed to join event ❌", Toast.LENGTH_SHORT).show();
                });
    }
}
