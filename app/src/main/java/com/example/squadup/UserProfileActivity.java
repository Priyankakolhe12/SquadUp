// UserProfileActivity.java
package com.example.squadup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView txtName, txtRole, txtCollege, txtDegreeYear, txtBio, txtSkills, txtStrengths, txtPreferredRoles, txtPreferredEvents, txtPreviousEvents, txtAchievements, txtAvailability, txtGithub, txtLinkedIn, txtPortfolio;
    private Button btnInvite;
    private String userId;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        imgProfile = findViewById(R.id.imgProfile);
        txtName = findViewById(R.id.txtName);
        txtRole = findViewById(R.id.txtRole);
        txtCollege = findViewById(R.id.txtCollege);
        txtDegreeYear = findViewById(R.id.txtDegreeYear);
        txtBio = findViewById(R.id.txtBio);
        txtSkills = findViewById(R.id.txtSkills);
        txtStrengths = findViewById(R.id.txtStrengths);
        txtPreferredRoles = findViewById(R.id.txtPreferredRoles);
        txtPreferredEvents = findViewById(R.id.txtPreferredEvents);
        txtPreviousEvents = findViewById(R.id.txtPreviousEvents);
        txtAchievements = findViewById(R.id.txtAchievements);
        txtAvailability = findViewById(R.id.txtAvailability);
        txtGithub = findViewById(R.id.txtGithub);
        txtLinkedIn = findViewById(R.id.txtLinkedIn);
        txtPortfolio = findViewById(R.id.txtPortfolio);
        btnInvite = findViewById(R.id.btnInvite);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");

        loadUserProfile();

        btnInvite.setOnClickListener(v -> sendInvite());

        txtGithub.setOnClickListener(v -> openLink(txtGithub.getText().toString()));
        txtLinkedIn.setOnClickListener(v -> openLink(txtLinkedIn.getText().toString()));
        txtPortfolio.setOnClickListener(v -> openLink(txtPortfolio.getText().toString()));
    }

    private void loadUserProfile() {
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                txtName.setText(documentSnapshot.getString("fullName"));
                txtRole.setText(documentSnapshot.getString("role"));
                txtCollege.setText(documentSnapshot.getString("college"));
                String degree = documentSnapshot.getString("degree");
                String year = documentSnapshot.getString("year");
                txtDegreeYear.setText(degree + " - " + year);
                txtBio.setText(documentSnapshot.getString("bio"));
                txtAvailability.setText(documentSnapshot.getString("availability"));

                txtSkills.setText(listToString(documentSnapshot.get("skills")));
                txtStrengths.setText(listToString(documentSnapshot.get("strengths")));
                txtPreferredRoles.setText(listToString(documentSnapshot.get("preferredRoles")));
                txtPreferredEvents.setText(listToString(documentSnapshot.get("preferredEvents")));
                txtPreviousEvents.setText(listToString(documentSnapshot.get("previousEvents")));
                txtAchievements.setText(documentSnapshot.getString("achievements"));

                txtGithub.setText(documentSnapshot.getString("github"));
                txtLinkedIn.setText(documentSnapshot.getString("linkedIn"));
                txtPortfolio.setText(documentSnapshot.getString("portfolio"));

                String imageUrl = documentSnapshot.getString("imageUrl");
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(this).load(imageUrl).placeholder(R.drawable.img_1).into(imgProfile);
                }
            }
        });
    }

    private void sendInvite() {
        String senderId = auth.getUid();

        Message inviteMessage = new Message(senderId, userId, "Hi, I'd like to invite you to my team!", System.currentTimeMillis());

        FirebaseDatabase.getInstance().getReference()
                .child("Chats")
                .child(senderId)
                .child(userId)
                .push()
                .setValue(inviteMessage)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Invite Sent 🚀", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to send invite", Toast.LENGTH_SHORT).show());
    }

    private void openLink(String url) {
        if (url != null && !url.isEmpty() && !url.equals("Link")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        }
    }

    private String listToString(Object listObject) {
        if (listObject instanceof ArrayList<?>) {
            ArrayList<?> list = (ArrayList<?>) listObject;
            return list.toString().replace("[", "").replace("]", "");
        }
        return "";
    }
}