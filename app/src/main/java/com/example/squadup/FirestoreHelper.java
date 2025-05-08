package com.example.squadup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class FirestoreHelper {
    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void addUser(String userId, String email, OnCompleteListener<Void> listener) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        db.collection("users").document(userId).set(user).addOnCompleteListener(listener);
    }

    public void addEvent(Event event, OnCompleteListener<DocumentReference> listener) {
        db.collection("events").add(event).addOnCompleteListener(listener);
    }


    public Query getEvents() {
        return db.collection("events");
    }
    
    public void updateEvent(String eventId, Map<String, Object> updates, OnCompleteListener<Void> listener) {
        DocumentReference eventRef = db.collection("events").document(eventId);
        eventRef.update(updates).addOnCompleteListener(listener);
    }
}
