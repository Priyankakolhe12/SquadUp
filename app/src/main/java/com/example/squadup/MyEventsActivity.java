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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private ArrayList<EventModel> myEventList = new ArrayList<>();
    private ProgressBar progressBar;

    private DatabaseReference eventsRef;
    private DatabaseReference joinedEventsRef;
    private FirebaseAuth auth;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getUid();

        eventsRef = FirebaseDatabase.getInstance().getReference("Events");
        joinedEventsRef = FirebaseDatabase.getInstance().getReference("JoinedEvents");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadMyEvents();
    }

    private void loadMyEvents() {
        progressBar.setVisibility(View.VISIBLE);

        joinedEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myEventList.clear();

                for (DataSnapshot eventSnap : snapshot.getChildren()) {
                    String eventId = eventSnap.getKey();
                    if (eventSnap.hasChild(currentUserId)) {
                        // User has joined this event
                        eventsRef.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot eventSnapshot) {
                                EventModel event = eventSnapshot.getValue(EventModel.class);
                                if (event != null) {
                                    event.setEventId(eventSnapshot.getKey());
                                    myEventList.add(event);

                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }

                if (!snapshot.hasChildren()) {
                    Toast.makeText(MyEventsActivity.this, "No joined events.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyEventsActivity.this, "Error loading joined events.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
