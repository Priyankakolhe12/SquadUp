package com.example.squadup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileStep1Activity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private EditText fullNameEditText, collegeEditText, roleEditText, yearEditText, degreeEditText, bioEditText;
    private Button continueButton;
    private Uri imageUri;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    private final String imgbbKey = "da083775856d5b18e6a014a9f11f5e9e"; // Your ImgBB API key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_step1);

        // Initialize Views
        initializeViews();

        // Firebase
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // Check if the user has already completed the profile setup
        checkIfProfileExists();

        // Click listener for picking an image
        profileImageView.setOnClickListener(v -> openImagePicker());

        // Click listener for continue button
        continueButton.setOnClickListener(v -> validateAndProceed());
    }

    private void initializeViews() {
        profileImageView = findViewById(R.id.profileImage);
        fullNameEditText = findViewById(R.id.editFullName);
        collegeEditText = findViewById(R.id.editCollege);
        roleEditText = findViewById(R.id.editRole);
        yearEditText = findViewById(R.id.editYear);
        degreeEditText = findViewById(R.id.editDegree);
        bioEditText = findViewById(R.id.editBio);
        continueButton = findViewById(R.id.btnContinue);
    }

    private void checkIfProfileExists() {
        String userId = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Profile exists, redirect to MainActivity
                        startActivity(new Intent(ProfileStep1Activity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ProfileStep1Activity.this, "Error checking profile", Toast.LENGTH_SHORT).show());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (reqCode == PICK_IMAGE_REQUEST && resCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void validateAndProceed() {
        String fullName = fullNameEditText.getText().toString().trim();
        String college = collegeEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();
        String year = yearEditText.getText().toString().trim();
        String degree = degreeEditText.getText().toString().trim();
        String bio = bioEditText.getText().toString().trim();

        if (fullName.isEmpty() || college.isEmpty() || role.isEmpty() ||
                year.isEmpty() || degree.isEmpty() || bio.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUri != null) {
            uploadImageToImgbb(fullName, college, role, year, degree, bio);
        } else {
            saveToFirestore(fullName, college, role, year, degree, bio, null);
        }
    }

    private void uploadImageToImgbb(String fullName, String college, String role, String year,
                                    String degree, String bio) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = getBytes(inputStream);
            String imageBase64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("key", imgbbKey)
                    .add("image", imageBase64)
                    .build();

            Request request = new Request.Builder()
                    .url("https://api.imgbb.com/1/upload")
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(ProfileStep1Activity.this,
                            "Image upload failed", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            String resp = response.body().string();
                            JSONObject json = new JSONObject(resp);
                            String imageUrl = json.getJSONObject("data").getString("url");

                            runOnUiThread(() -> saveToFirestore(fullName, college, role, year, degree, bio, imageUrl));
                        } catch (JSONException e) {
                            runOnUiThread(() -> Toast.makeText(ProfileStep1Activity.this,
                                    "Parsing error", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        runOnUiThread(() -> Toast.makeText(ProfileStep1Activity.this,
                                "Upload failed: Server error", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not process image", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, bytesRead);
        }
        return byteBuffer.toByteArray();
    }

    private void saveToFirestore(String fullName, String college, String role, String year,
                                 String degree, String bio, String imageUrl) {

        String userId = firebaseAuth.getCurrentUser().getUid();
        UserProfile profile = new UserProfile(fullName, college, role, year, degree, bio, imageUrl);

        firestore.collection("users").document(userId).set(profile)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(ProfileStep1Activity.this, "Profile Saved!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProfileStep1Activity.this, ProfileStep2Activity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(ProfileStep1Activity.this,
                        "Failed to save profile", Toast.LENGTH_SHORT).show());
    }

    // Data Model Class
    public static class UserProfile {
        public String fullName, college, role, year, degree, bio, imageUrl;

        public UserProfile() {
            // Empty constructor required for Firestore
        }

        public UserProfile(String fullName, String college, String role, String year,
                           String degree, String bio, String imageUrl) {
            this.fullName = fullName;
            this.college = college;
            this.role = role;
            this.year = year;
            this.degree = degree;
            this.bio = bio;
            this.imageUrl = imageUrl;
        }
    }
}
