package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    TextView oTextViewRegister;
    TextInputEditText oTextInputEditTextEmail;
    TextInputEditText oTextInputEditTextPassword;
    Button oButtonLogin;
    FirebaseAuth oAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private final int REQUEST_CODE_GOOGLE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oTextViewRegister=findViewById(R.id.TextViewRegister);
        oTextInputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        oTextInputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        oButtonLogin=findViewById(R.id.btnlogin);
        oAuth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        oButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
        oTextViewRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

    }
    private void singInGoogle(){
        Intent signIntent =mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, REQUEST_CODE_GOOGLE);
    }
    private void firebaseAuthWhitGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        oAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String id=oAuth.getCurrentUser().getUid();
                            checkUserExist(id);
                        }else{
                            Log.w("ERROR", "signInWithCredential:faulure", task.getException());
                        }

                    }
                });
    }

    private void checkUserExist(String id) {

    }

    private void login() {
        String email = oTextInputEditTextEmail.getText().toString();
        String password = oTextInputEditTextPassword.getText().toString();

        oAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent =new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Email o contraseña no son correctos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d("campo", "email" + email);
        Log.d("campo", "password" + password);
    }

}