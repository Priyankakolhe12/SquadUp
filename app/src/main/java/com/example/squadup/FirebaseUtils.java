package com.example.squadup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUtils {

    public static void updateUserToken(String token) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (uid != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("fcmToken", token);

            FirebaseFirestore.getInstance().collection("users")
                    .document(uid)
                    .update(map);
        }
    }
}
