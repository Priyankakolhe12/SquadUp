package com.example.squadup;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateEventActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput, dateInput, timeInput, locationInput, categoryInput;
    private Button btnSubmit;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        titleInput = findViewById(R.id.eventTitle);
        descriptionInput = findViewById(R.id.eventDescription);
        dateInput = findViewById(R.id.eventDate);
        timeInput = findViewById(R.id.eventTime);
        locationInput = findViewById(R.id.eventLocation);
        categoryInput = findViewById(R.id.eventCategory);
        btnSubmit = findViewById(R.id.btnSubmitEvent);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnSubmit.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String desc = descriptionInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();
            String category = categoryInput.getText().toString().trim();

            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)) {
                Toast.makeText(this, "Title and Description required", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> event = new HashMap<>();
            event.put("title", title);
            event.put("description", desc);
            event.put("date", date);
            event.put("time", time);
            event.put("location", location);
            event.put("category", category);
            event.put("createdBy", auth.getCurrentUser().getUid());

            db.collection("events").add(event).addOnSuccessListener(ref -> {
                Toast.makeText(this, "Event Created!", Toast.LENGTH_SHORT).show();
                finish();
            });
        });
    }
}
