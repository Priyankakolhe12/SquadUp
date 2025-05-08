package com.example.squadup;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // User is signed in
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
        } else {
            // No user is signed in
            startActivity(new Intent(LauncherActivity.this, WelcomeActivity.class));
        }

        finish(); // Prevent coming back to this screen
    }
}
