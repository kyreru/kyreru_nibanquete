package com.example.myapplication.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProviders {
    private FirebaseAuth oAuth;

    public AuthProviders() {
        oAuth=FirebaseAuth.getInstance();
    }
    public Task<AuthResult> login (String email, String password){
       return oAuth.signInWithEmailAndPassword(email,password);

    }
    public Task<AuthResult> register(String email, String password){
        return oAuth.createUserWithEmailAndPassword(email, password);
    }
    public Task<AuthResult>googleLogin(GoogleSignInAccount googleSignInAccount){
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return oAuth.signInWithCredential(credential);
    }
    public String getEmail(){
        if (oAuth.getCurrentUser() !=null){
            return oAuth.getCurrentUser().getEmail();
        }else{
            return null;
        }
    }
    public String getVid(){
        if (oAuth.getCurrentUser() !=null){
            return oAuth.getCurrentUser().getUid();
        }else {
            return null;
        }
    }
}


