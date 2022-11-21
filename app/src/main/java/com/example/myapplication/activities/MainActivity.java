package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.providers.AuthProviders;
import com.example.myapplication.providers.UsersProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView oTextViewRegister;
    TextInputEditText oTextInputEditTextEmail;
    TextInputEditText oTextInputEditTextPassword;
    Button oButtonLogin;
    SignInButton mbtngoogle;
    AuthProviders oAuthProviders;
    private GoogleSignInClient mGoogleSignInClient;
    private final int REQUEST_CODE_GOOGLE=1;
    UsersProviders oUsersproviders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oTextViewRegister=findViewById(R.id.TextViewRegister);
        oTextInputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        oTextInputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        oButtonLogin=findViewById(R.id.btnlogin);
        mbtngoogle=findViewById(R.id.btnloginSignInGoogle);

        oAuthProviders=new AuthProviders();
        oUsersproviders=new UsersProviders();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        oButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { login();}

        });

        mbtngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singInGoogle();
            }
        });

        oTextViewRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

    }

    private void singInGoogle(){
        Intent signInIntent =mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("ERROR", "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        oAuthProviders.googleLogin(account)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String id=oAuthProviders.getVid();
                            checkUserExist(id);
                        }else{
                            Log.w("ERROR", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this,"no se pudo iniciar sesion google", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void checkUserExist(String id) {
        oUsersproviders.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    Intent intent=new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else {
                    String email=oAuthProviders.getEmail();
                    User user=new User();
                    user.setEmail(email);
                    user.setId(id);
                    //Map<String,Object> map=new HashMap<>();
                    //map.put("email",email);
                    oUsersproviders.create(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent=new Intent(MainActivity.this, CompletarregistroActivity2.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this,"no se pudo almacenar informacion",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    private void login() {
        String email = oTextInputEditTextEmail.getText().toString();
        String password = oTextInputEditTextPassword.getText().toString();
        oAuthProviders.login(email, password).
        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

