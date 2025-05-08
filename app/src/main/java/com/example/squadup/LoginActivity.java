package com.example.squadup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView signupText, forgotPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        signupText = findViewById(R.id.signupText);
        forgotPassword = findViewById(R.id.forgotPassword);

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUserWithEmailPassword(email, password);
            }
        });

        signupText.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void loginUserWithEmailPassword(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            checkUserProfile(user.getUid());
                        }
                    } else {
                        Toast.makeText(this, "Authentication failed. Check email or password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserProfile(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Intent intent;
                    if (documentSnapshot.exists() &&
                            documentSnapshot.contains("fullName") &&
                            documentSnapshot.contains("skills")) {
                        intent = new Intent(this, MainActivity.class);
                    } else {
                        intent = new Intent(this, ProfileStep1Activity.class);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to check profile", Toast.LENGTH_SHORT).show());
    }
}
