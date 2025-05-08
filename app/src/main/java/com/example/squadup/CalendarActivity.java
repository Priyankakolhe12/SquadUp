package com.example.squadup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {

    private Button btnPickDate;
    private FirebaseFirestore db;
    private HashMap<String, ArrayList<Event>> eventsByDate = new HashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btnPickDate = findViewById(R.id.btnPickDate);
        db = FirebaseFirestore.getInstance();

        loadEvents();

        btnPickDate.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker =
                    MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select a Date")
                            .build();

            datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                Date selectedDate = new Date(selection);
                String formattedDate = sdf.format(selectedDate);

                if (eventsByDate.containsKey(formattedDate)) {
                    ArrayList<Event> events = eventsByDate.get(formattedDate);
                    showEventsDialog(events);
                } else {
                    Toast.makeText(CalendarActivity.this, "No events on this date", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadEvents() {
        db.collection("events")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Event event = doc.toObject(Event.class);

                        if (event.getDate() != null) {
                            if (!eventsByDate.containsKey(event.getDate())) {
                                eventsByDate.put(event.getDate(), new ArrayList<>());
                            }
                            eventsByDate.get(event.getDate()).add(event);

                        }
                    }
                });
    }

    private void showEventsDialog(ArrayList<Event> events) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Events on selected date");

        StringBuilder message = new StringBuilder();
        for (Event event : events) {
            message.append("• ").append(event.getTitle()).append("\n");
        }

        builder.setMessage(message.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
