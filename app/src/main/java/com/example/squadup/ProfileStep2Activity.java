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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileStep2Activity extends AppCompatActivity {

    EditText editLinkedIn, editGithub, editPortfolio, editAchievements;
    TextView tagReact, tagPython, tagUIUX;
    Button btnContinue, btnAddMore;
    LinearLayout skillsContainer;
    FirebaseFirestore db;
    ArrayList<String> selectedSkills = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_step2);

        db = FirebaseFirestore.getInstance();

        editLinkedIn = findViewById(R.id.editLinkedIn);
        editGithub = findViewById(R.id.editGithub);
        editPortfolio = findViewById(R.id.editPortfolio);
        editAchievements = findViewById(R.id.editAchievements);

        tagReact = findViewById(R.id.tagReact);
        tagPython = findViewById(R.id.tagPython);
        tagUIUX = findViewById(R.id.tagUIUX);
        skillsContainer = findViewById(R.id.skillsContainer);

        btnContinue = findViewById(R.id.btnContinue2);
        btnAddMore = findViewById(R.id.btnAddMoreSkills);

        setupTagToggle(tagReact, "React");
        setupTagToggle(tagPython, "Python");
        setupTagToggle(tagUIUX, "UI/UX");

        btnAddMore.setOnClickListener(v -> showSkillInputDialog());

        btnContinue.setOnClickListener(v -> saveProfileData());
    }

    private void setupTagToggle(TextView tagView, String skillName) {
        tagView.setOnClickListener(v -> toggleSkill(tagView, skillName));
    }

    private void toggleSkill(TextView tagView, String skillName) {
        if (selectedSkills.contains(skillName)) {
            selectedSkills.remove(skillName);
            tagView.setBackgroundResource(R.drawable.tag_bg);
            tagView.setTextColor(Color.parseColor("#7A5FFF"));
        } else {
            selectedSkills.add(skillName);
            GradientDrawable drawable = (GradientDrawable) tagView.getBackground().mutate();
            drawable.setColor(Color.parseColor("#7A5FFF"));
            tagView.setBackground(drawable);
            tagView.setTextColor(Color.WHITE);
        }
    }

    private void showSkillInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter a new skill");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("e.g., Kotlin, ML, etc.");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String newSkill = input.getText().toString().trim();
            if (!newSkill.isEmpty()) {
                addCustomSkill(newSkill);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addCustomSkill(String skill) {
        TextView newTag = new TextView(this);
        newTag.setText(skill);
        newTag.setPadding(16, 8, 16, 8);
        newTag.setBackgroundResource(R.drawable.tag_bg);
        newTag.setTextColor(Color.parseColor("#7A5FFF"));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 16);
        newTag.setLayoutParams(layoutParams);

        newTag.setOnClickListener(v -> toggleSkill(newTag, skill));

        skillsContainer.addView(newTag);
    }

    private void saveProfileData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String linkedIn = editLinkedIn.getText().toString().trim();
        String github = editGithub.getText().toString().trim();
        String portfolio = editPortfolio.getText().toString().trim();
        String achievements = editAchievements.getText().toString().trim();

        if (linkedIn.isEmpty() || github.isEmpty() || portfolio.isEmpty() || achievements.isEmpty() || selectedSkills.isEmpty()) {
            Toast.makeText(this, "Please fill all fields and select skills!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("linkedIn", linkedIn);
        data.put("github", github);
        data.put("portfolio", portfolio);
        data.put("achievements", achievements);
        data.put("skills", selectedSkills);

        db.collection("users").document(uid)
                .update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Step 2 saved!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileStep2Activity.this, ProfileStep3Activity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}