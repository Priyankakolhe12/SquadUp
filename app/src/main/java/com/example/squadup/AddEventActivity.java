package com.example.squadup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDate, etMode, etSkills, etLink, etCollege, etLastDate, etNotes;
    private Button btnCreateEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etMode = findViewById(R.id.etMode);
        etSkills = findViewById(R.id.etSkills);
        etLink = findViewById(R.id.etLink);
        etCollege = findViewById(R.id.etCollege);
        etLastDate = findViewById(R.id.etLastDate);
        etNotes = findViewById(R.id.etNotes);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        btnCreateEvent.setOnClickListener(v -> createEvent());
    }

    private void createEvent() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String mode = etMode.getText().toString().trim();
        String skills = etSkills.getText().toString().trim();
        String link = etLink.getText().toString().trim();
        String college = etCollege.getText().toString().trim();
        String lastDate = etLastDate.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || date.isEmpty() || mode.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> event = new HashMap<>();
        event.put("title", title);
        event.put("description", description);
        event.put("date", date);
        event.put("mode", mode);
        event.put("skills", skills);
        event.put("link", link);
        event.put("college", college);
        event.put("lastDate", lastDate);
        event.put("notes", notes);

        FirebaseDatabase.getInstance().getReference("Events")
                .push()
                .setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Event Created Successfully 🚀", Toast.LENGTH_SHORT).show();
                    finish(); // go back to event list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to create event!", Toast.LENGTH_SHORT).show();
                });
    }
}
