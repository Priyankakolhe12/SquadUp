package com.example.squadup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseAuthHelper {
    private final FirebaseAuth mAuth;
    private final GoogleSignInClient mGoogleSignInClient;

    public FirebaseAuthHelper(Context context) {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(context, gso);
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void signInWithGoogle(GoogleSignInAccount account, OnCompleteListener<com.google.firebase.auth.AuthResult> listener) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(listener);
    }

    public void login(String email, String password, OnCompleteListener<com.google.firebase.auth.AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void signUp(String email, String password, OnCompleteListener<com.google.firebase.auth.AuthResult> listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    public void resetPassword(String email, OnCompleteListener<Void> listener) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(listener);
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();
    }
}
