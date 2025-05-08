package com.example.squadup;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    // UI Components
    private EditText editFullName, editContact, editEmail, editDOB, editPassword, editConfirmPassword;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private RadioGroup genderGroup;
    private ImageView eyePassword;
    private boolean isPasswordVisible = false;
    private TextView tvSignIn;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        editFullName = findViewById(R.id.editFullName);
        editContact = findViewById(R.id.editContact);
        editEmail = findViewById(R.id.editEmail);
        editDOB = findViewById(R.id.editDOB);
        editPassword = findViewById(R.id.editPassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progressBar);
        genderGroup = findViewById(R.id.genderGroup);
        eyePassword = findViewById(R.id.eyePassword);
        tvSignIn = findViewById(R.id.tvSignIn);

        // Toggle password visibility
        eyePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                eyePassword.setImageResource(R.drawable.ic_visibility_off);
            } else {
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                eyePassword.setImageResource(R.drawable.ic_visibility);
            }
            editPassword.setSelection(editPassword.getText().length());
            isPasswordVisible = !isPasswordVisible;
        });

        // Open DatePicker on DOB field click
        editDOB.setOnClickListener(v -> showDatePickerDialog());

        // Sign Up button action
        btnSignUp.setOnClickListener(v -> {
            String fullName = editFullName.getText().toString().trim();
            String contact = editContact.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String dob = editDOB.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            String confirmPassword = editConfirmPassword.getText().toString().trim();
            String gender = getSelectedGender();

            if (validateInputs(fullName, contact, email, dob, password, confirmPassword, gender)) {
                progressBar.setVisibility(View.VISIBLE);
                registerUserWithEmail(fullName, email, password);
            }
        });

        // Sign in redirect
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(RegisterActivity.this, (view, year1, month1, dayOfMonth) -> {
            String dob = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            editDOB.setText(dob);
        }, year, month, day).show();
    }

    private String getSelectedGender() {
        int selectedId = genderGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadio = findViewById(selectedId);
            return selectedRadio.getText().toString();
        }
        return null;
    }

    private boolean validateInputs(String fullName, String contact, String email, String dob, String password, String confirmPassword, String gender) {
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(dob) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || gender == null) {
            showToast("Please fill in all fields");
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Enter a valid email address");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }

        if (password.length() < 6) {
            showToast("Password must be at least 6 characters");
            return false;
        }

        if (!contact.matches("[0-9]{10}")) {
            showToast("Enter a valid 10-digit contact number");
            return false;
        }

        return true;
    }

    private void registerUserWithEmail(String fullName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        showToast("Registration Successful!");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        showToast("Registration Failed: " + task.getException().getMessage());
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
