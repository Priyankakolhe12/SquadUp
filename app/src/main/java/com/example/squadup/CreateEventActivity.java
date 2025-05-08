package com.example.squadup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventTitle, eventDescription, eventDate, eventLink, collegeName, lastDate, additionalNotes;
    private Spinner modeSpinner;
    private Button btnAddSkill, btnSave;
    private LinearLayout skillsLayout;
    private ArrayList<String> skillsList = new ArrayList<>();
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        auth = FirebaseAuth.getInstance();

        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        eventDate = findViewById(R.id.eventDate);
        eventLink = findViewById(R.id.eventLink);
        collegeName = findViewById(R.id.collegeName);
        lastDate = findViewById(R.id.lastDate);
        additionalNotes = findViewById(R.id.additionalNotes);
        modeSpinner = findViewById(R.id.modeSpinner);
        btnAddSkill = findViewById(R.id.btnAddSkill);
        btnSave = findViewById(R.id.btnSave);
        skillsLayout = findViewById(R.id.skillsLayout);

        eventDate.setOnClickListener(v -> openDatePicker(eventDate));
        lastDate.setOnClickListener(v -> openDatePicker(lastDate));

        btnAddSkill.setOnClickListener(v -> {
            EditText skillInput = findViewById(R.id.skillInput);
            String skill = skillInput.getText().toString().trim();
            if (!skill.isEmpty()) {
                skillsList.add(skill);
                TextView skillTag = new TextView(this);
                skillTag.setText(skill);
                skillTag.setBackgroundResource(R.drawable.skill_chip_bg);
                skillTag.setPadding(16, 8, 16, 8);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 8, 8, 8);
                skillTag.setLayoutParams(params);
                skillsLayout.addView(skillTag);
                skillInput.setText("");
            }
        });

        btnSave.setOnClickListener(v -> saveEvent());
    }

    private void openDatePicker(EditText targetEditText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(CreateEventActivity.this, (view, year, month, dayOfMonth) -> {
            targetEditText.setText((month + 1) + "/" + dayOfMonth + "/" + year);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveEvent() {
        String title = eventTitle.getText().toString();
        String description = eventDescription.getText().toString();
        String date = eventDate.getText().toString();
        String mode = modeSpinner.getSelectedItem().toString();
        String link = eventLink.getText().toString();
        String college = collegeName.getText().toString();
        String lastRegDate = lastDate.getText().toString();
        String notes = additionalNotes.getText().toString();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventId = FirebaseDatabase.getInstance().getReference("Events").push().getKey();
        Event event = new Event(eventId, title, description, date, mode, skillsList, link, college, lastRegDate, notes, "https://via.placeholder.com/150");

        FirebaseDatabase.getInstance().getReference("Events")
                .child(eventId)
                .setValue(event)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(CreateEventActivity.this, "Event Created 🚀", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CreateEventActivity.this, EventListActivity.class));
                    finish();
                });
    }
}
