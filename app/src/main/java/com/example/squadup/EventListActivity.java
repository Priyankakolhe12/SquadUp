package com.example.squadup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private ArrayList<EventModel> eventList = new ArrayList<>();
    private DatabaseReference eventsRef;
    private FloatingActionButton fabAddEvent;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        eventsRef = FirebaseDatabase.getInstance().getReference("Events");

        loadEvents();

        fabAddEvent.setOnClickListener(v -> {
            startActivity(new Intent(EventListActivity.this, AddEventActivity.class));
        });
        Button btnMyEvents = findViewById(R.id.btnMyEvents);

        btnMyEvents.setOnClickListener(v -> {
            startActivity(new Intent(EventListActivity.this, MyEventsActivity.class));
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadEvents() {
        progressBar.setVisibility(View.VISIBLE);

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    EventModel event = snap.getValue(EventModel.class);
                    if (event != null) {
                        event.setEventId(snap.getKey()); // store event id for join/view
                        eventList.add(event);
                    }
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
