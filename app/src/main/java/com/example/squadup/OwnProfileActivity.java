package com.example.squadup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class OwnProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private TextView txtName, txtRole, txtCollege, txtSkills, txtBio;
    private Button btnEditProfile, btnLogout;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_profile);

        imgProfile = findViewById(R.id.imgProfile);
        txtName = findViewById(R.id.txtName);
        txtRole = findViewById(R.id.txtRole);
        txtCollege = findViewById(R.id.txtCollege);
        txtSkills = findViewById(R.id.txtSkills);
        txtBio = findViewById(R.id.txtBio);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        loadUserProfile();

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(OwnProfileActivity.this, ProfileStep1Activity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        auth.signOut();
                        startActivity(new Intent(OwnProfileActivity.this, WelcomeActivity.class));
                        finishAffinity();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void loadUserProfile() {
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            txtName.setText(user.getFullName());
                            txtRole.setText(user.getRole());
                            txtCollege.setText(user.getCollege());
                            txtBio.setText(user.getBio());
                            if (user.getSkills() != null) {
                                txtSkills.setText(user.getSkills().toString().replace("[", "").replace("]", ""));
                            }
                            if (user.getImageUrl() != null && !user.getImageUrl().isEmpty()) {
                                Glide.with(this).load(user.getImageUrl()).into(imgProfile);
                            } else {
                                imgProfile.setImageResource(R.drawable.img_1);
                            }
                        }
                    }
                });
    }
}
