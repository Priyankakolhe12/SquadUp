package com.example.squadup;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        resetBtn = findViewById(R.id.resetBtn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Enter your registered email", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Reset link sent to your email", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Error! Reset link not sent", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
