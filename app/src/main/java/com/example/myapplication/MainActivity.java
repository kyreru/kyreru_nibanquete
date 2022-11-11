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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView oTextViewRegister;
    TextInputEditText oTextInputEditTextEmail;
    TextInputEditText oTextInputEditTextPassword;
    Button oButtonLogin;
    FirebaseAuth oAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oTextViewRegister=findViewById(R.id.TextViewRegister);
        oTextInputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        oTextInputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        oButtonLogin=findViewById(R.id.btnlogin);
        oAuth=FirebaseAuth.getInstance();

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