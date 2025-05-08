package com.example.squadup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private ArrayList<User> userList = new ArrayList<>();
    private ProgressBar progressBar;
    private EditText searchEditText;
    private ImageView btnEditProfile, btnNotifications, profileImage;
    private TextView txtName, txtRole, welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnNotifications = findViewById(R.id.btnNotifications);
        profileImage = findViewById(R.id.profileImage);
        txtName = findViewById(R.id.txtName);
        txtRole = findViewById(R.id.txtRole);
        welcomeText = findViewById(R.id.welcomeText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(userAdapter);

        loadCurrentUser();
        loadUsers();

        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OwnProfileActivity.class));
        });

        btnNotifications.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        });

        setupBottomNavigation();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void loadCurrentUser() {
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        txtName.setText(documentSnapshot.getString("fullName"));
                        txtRole.setText(documentSnapshot.getString("role"));
                        welcomeText.setText("Welcome to SquadUp 👋");

                        String imageUrl = documentSnapshot.getString("imageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.img_1)
                                    .into(profileImage);
                        }
                    }
                });
    }

    private void loadUsers() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        userList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (!doc.getId().equals(auth.getCurrentUser().getUid())) {
                                try {
                                    User user = doc.toObject(User.class);
                                    if (user != null) {
                                        user.setUid(doc.getId());
                                        userList.add(user);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void filterUsers(String query) {
        ArrayList<User> filteredList = new ArrayList<>();
        for (User user : userList) {
            String fullName = user.getFullName() != null ? user.getFullName() : "";
            String skills = user.getSkills() != null ? user.getSkills().toString() : "";
            String role = user.getRole() != null ? user.getRole() : "";

            if (fullName.toLowerCase().contains(query.toLowerCase()) ||
                    skills.toLowerCase().contains(query.toLowerCase()) ||
                    role.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        userAdapter.filterList(filteredList);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_events) {
                startActivity(new Intent(this, EventListActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_invites) {
                startActivity(new Intent(this, InvitesActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_team) {
                startActivity(new Intent(this, TeamActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }
}
