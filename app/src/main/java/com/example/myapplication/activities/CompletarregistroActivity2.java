package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.User;
import com.example.myapplication.providers.AuthProviders;
import com.example.myapplication.providers.UsersProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class CompletarregistroActivity2 extends AppCompatActivity {
    TextInputEditText oTextInputUsernameRegister;
    Button oButtonRegister;
    AuthProviders oAuthProviders;
    UsersProviders oUsersproviders;
    AlertDialog oDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completarregistro2);

        oTextInputUsernameRegister=findViewById(R.id.textInputEditTextUserNameCompletar);
        oButtonRegister= findViewById(R.id.ButtonRegisterCompletar);

        oAuthProviders=new AuthProviders();
        oUsersproviders=new UsersProviders();
        oDialog =new SpotsDialog.Builder()
                .setContext(this)
                .setMessage(R.string.custom_title)
                .setCancelable(false)
                .build();

        oButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String username=oTextInputUsernameRegister.getText().toString();
        if(!username.isEmpty()){
            updateUser(username);
        }else{
            Toast.makeText(this, "Para continuar ingrese Usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(final String username) {
        String id=oAuthProviders.getVid();
        User user=new User();
        user.setUsername(username);
        user.setId(id);
        oDialog.show();
        oUsersproviders.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                oDialog.dismiss();
                if(task.isSuccessful()){
                    Intent intent=new Intent(CompletarregistroActivity2.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(CompletarregistroActivity2.this, "No se guardo en base datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}


