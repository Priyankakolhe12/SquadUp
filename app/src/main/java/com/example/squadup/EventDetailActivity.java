package com.example.squadup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvDescription, tvDate;
    private ImageView ivEventImage;
    private Button btnDeleteEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        ivEventImage = findViewById(R.id.ivEventImage);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        // Get data from intent
        String title = getIntent().getStringExtra("event_title");
        String desc = getIntent().getStringExtra("event_description");
        String date = getIntent().getStringExtra("event_date");
        String image = getIntent().getStringExtra("event_image");
        final String eventId = getIntent().getStringExtra("event_id"); // Make sure to pass event_id

        tvTitle.setText(title);
        tvDescription.setText(desc);
        tvDate.setText(date);

        if (image != null && !image.isEmpty()) {
            Glide.with(this).load(image).into(ivEventImage);
        }

        btnDeleteEvent.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Use eventId to delete the correct document
                        FirebaseFirestore.getInstance().collection("events")
                                .document(eventId)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show();
                                    finish(); // Close detail screen
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Failed to delete event", Toast.LENGTH_SHORT).show()
                                );
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
