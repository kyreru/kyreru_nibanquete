package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.providers.ImageProviders;
import com.example.myapplication.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import javax.annotation.Nullable;

public class PostActivity extends AppCompatActivity {
    ImageView oImageViewPost1;
    ImageView oImageViewPost2;
    File oImageFile;
    private final int Gallery_REQUEST_CODE=1;
    Button oButtonPost;
    ImageProviders oImageProviders;
    TextInputEditText oTextInputTitle;
    TextInputEditText oDescription;
    ImageView oImageViewPintura;
    ImageView oImageViewEscultura;
    ImageView oImageViewFotografia;
    ImageView oImageViewArtDigital;
    TextView oTextViewCategory;
    String oCategory="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        oImageViewPost1=findViewById(R.id.imageViewPost1);

        oTextInputTitle=findViewById(R.id.textInputEditTextTitulo);
        oDescription=findViewById(R.id.textInputEditTextDescripcion);
        oImageViewPintura=findViewById(R.id.Pintura);
        oImageViewEscultura=findViewById(R.id.Escultura);
        oImageViewFotografia=findViewById(R.id.fotografia);
        oImageViewArtDigital=findViewById(R.id.artDigital);
        oTextViewCategory=findViewById(R.id.textViewCardCategory);


        oButtonPost=findViewById(R.id.BtnPost);

        oImageProviders=new ImageProviders();

        oButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveImagen();
                clickPost();
            }
        });

        oImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        oImageViewPintura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCategory="Pintura";
            }
        });
        oImageViewEscultura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCategory="Escultura";
            }
        });
        oImageViewFotografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCategory="Fotografia";
            }
        });
        oImageViewArtDigital.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCategory="ArteDigital";
            }
        }));
    }

    private void clickPost() {
        String title=oTextInputTitle.getText().toString();
        String Description=oDescription.getText().toString();

    }

    private void saveImagen() {
        oImageProviders.save(PostActivity.this,oImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
             if (task.isSuccessful()){
                 Toast.makeText(PostActivity.this,"La imagen se almaceno", Toast.LENGTH_SHORT).show();
             }else {
                 Toast.makeText(PostActivity.this,"Error al cargar imagen", Toast.LENGTH_SHORT).show();
             }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if (requestCode == Gallery_REQUEST_CODE && resultCode ==RESULT_OK){
            try {
                oImageFile = FileUtil.from(this, data.getData());
                oImageViewPost1 .setImageBitmap(BitmapFactory.decodeFile(oImageFile.getAbsolutePath()));
            }catch (Exception e) {
                Log.d("error", "Se produjo un error" + e.getMessage());
                Toast.makeText(this, "Se produjo un error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}