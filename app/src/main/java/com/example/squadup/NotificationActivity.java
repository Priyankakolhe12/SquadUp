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

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private NotificationAdapter adapter;
    private ArrayList<NotificationModel> notificationList = new ArrayList<>();
    private DatabaseReference notificationsRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications").child(auth.getUid());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(adapter);

        loadNotifications();
    }

    private void loadNotifications() {
        progressBar.setVisibility(View.VISIBLE);

        notificationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                notificationList.clear();

                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        NotificationModel notification = snap.getValue(NotificationModel.class);
                        if (notification != null) {
                            notificationList.add(notification);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NotificationActivity.this, "No notifications found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(NotificationActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
