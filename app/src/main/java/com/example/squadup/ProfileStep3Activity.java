package com.example.squadup;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileStep3Activity extends AppCompatActivity {

    EditText editPreferredEvents, editPreferredRoles, editAvailability;
    Button btnAddMorePreviousEvents, btnAddMoreStrengths, btnFinish, btnSkip;
    LinearLayout previousEventsContainer, strengthsContainer;

    FirebaseFirestore db;
    ArrayList<String> previousEvents = new ArrayList<>();
    ArrayList<String> strengths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_step3);

        db = FirebaseFirestore.getInstance();

        editPreferredEvents = findViewById(R.id.editPreferredEvents);
        editPreferredRoles = findViewById(R.id.editPreferredRoles);
        editAvailability = findViewById(R.id.editAvailability);

        previousEventsContainer = findViewById(R.id.previousEventsContainer);
        strengthsContainer = findViewById(R.id.strengthsContainer);

        btnAddMorePreviousEvents = findViewById(R.id.btnAddMorePreviousEvents);
        btnAddMoreStrengths = findViewById(R.id.btnAddMoreStrengths);
        btnFinish = findViewById(R.id.btnFinish);

        btnAddMorePreviousEvents.setOnClickListener(v -> showTagInputDialog("Previous Event", previousEventsContainer, previousEvents));
        btnAddMoreStrengths.setOnClickListener(v -> showTagInputDialog("Strength", strengthsContainer, strengths));

        btnFinish.setOnClickListener(v -> saveProfileStep3());
    }

    private void showTagInputDialog(String title, LinearLayout container, ArrayList<String> list) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter " + title);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String tag = input.getText().toString().trim();
            if (!tag.isEmpty()) {
                addTag(tag, container, list);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addTag(String tagText, LinearLayout container, ArrayList<String> list) {
        TextView tag = new TextView(this);
        tag.setText(tagText);
        tag.setPadding(16, 8, 16, 8);
        tag.setBackgroundResource(R.drawable.tag_bg);
        tag.setTextColor(Color.parseColor("#7A5FFF"));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 16);
        tag.setLayoutParams(layoutParams);

        tag.setOnClickListener(v -> {
            if (list.contains(tagText)) {
                list.remove(tagText);
                tag.setBackgroundResource(R.drawable.tag_bg);
                tag.setTextColor(Color.parseColor("#7A5FFF"));
            } else {
                list.add(tagText);
                GradientDrawable drawable = (GradientDrawable) tag.getBackground().mutate();
                drawable.setColor(Color.parseColor("#7A5FFF"));
                tag.setBackground(drawable);
                tag.setTextColor(Color.WHITE);
            }
        });

        container.addView(tag);
        list.add(tagText);
    }

    private void saveProfileStep3() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String preferredEventsText = editPreferredEvents.getText().toString().trim();
        String preferredRolesText = editPreferredRoles.getText().toString().trim();
        String availability = editAvailability.getText().toString().trim();

        if (preferredEventsText.isEmpty() || preferredRolesText.isEmpty() || availability.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (previousEvents.isEmpty() || strengths.isEmpty()) {
            Toast.makeText(this, "Please add previous events and strengths!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Split text by comma to create List
        ArrayList<String> preferredEvents = new ArrayList<>();
        for (String event : preferredEventsText.split(",")) {
            preferredEvents.add(event.trim());
        }

        ArrayList<String> preferredRoles = new ArrayList<>();
        for (String role : preferredRolesText.split(",")) {
            preferredRoles.add(role.trim());
        }

        Map<String, Object> data = new HashMap<>();
        data.put("preferredEvents", preferredEvents);
        data.put("preferredRoles", preferredRoles);
        data.put("availability", availability);
        data.put("previousEvents", previousEvents);
        data.put("strengths", strengths);

        db.collection("users").document(uid)
                .update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Profile Completed!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
