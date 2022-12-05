package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.models.Post;
import com.example.myapplication.providers.AuthProviders;
import com.example.myapplication.providers.ImageProviders;
import com.example.myapplication.providers.PostProvider;
import com.example.myapplication.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    TextInputEditText oInputDescription;
    ImageView oImageViewPintura;
    ImageView oImageViewEscultura;
    ImageView oImageViewFotografia;
    ImageView oImageViewArtDigital;
    TextView oTextViewCategory;
    String oCategory="";
    PostProvider oPostProvider;
    String oTitle="";
    String oDescription="";
    AuthProviders oAuthProviders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        oImageViewPost1=findViewById(R.id.imageViewPost1);

        oTextInputTitle=findViewById(R.id.textInputEditTextTitulo);
        oInputDescription=findViewById(R.id.textInputEditTextDescripcion);
        oImageViewPintura=findViewById(R.id.Pintura);
        oImageViewEscultura=findViewById(R.id.Escultura);
        oImageViewFotografia=findViewById(R.id.fotografia);
        oImageViewArtDigital=findViewById(R.id.artDigital);
        oTextViewCategory=findViewById(R.id.textViewCardCategory);

        oPostProvider=new PostProvider();

        oButtonPost=findViewById(R.id.BtnPost);

        oImageProviders=new ImageProviders();
        oAuthProviders=new AuthProviders();

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
                oTextViewCategory.setText(oCategory);
            }
        });
        oImageViewEscultura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCategory="Escultura";
                oTextViewCategory.setText(oCategory);
            }
        });
        oImageViewFotografia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCategory="Fotografia";
                oTextViewCategory.setText(oCategory);
            }
        });
        oImageViewArtDigital.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oCategory="ArteDigital";
                oTextViewCategory.setText(oCategory);
            }
        }));
    }

    private void clickPost() {
        oTitle=oTextInputTitle.getText().toString();
        oDescription=oInputDescription.getText().toString();
        if (!oTitle.isEmpty() && !oDescription.isEmpty() && !oCategory.isEmpty()){
            if ( oImageFile !=null){
                saveImagen();

            }else{
                Toast.makeText(this,"Seleccione una imagen",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText( this,"Completar todos los campos",Toast.LENGTH_SHORT).show();
        }

    }

    private void saveImagen() {
        oImageProviders.save(PostActivity.this,oImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
             if (task.isSuccessful()){
                 oImageProviders.getoStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                     @Override
                     public void onSuccess(Uri uri) {
                         String url=uri.toString();
                         Post post=new Post();
                         post.setImage1(url);
                         post.setTitle(oTitle);
                         post.setDescription(oDescription);
                         post.setCategory(oCategory);
                         post.setIdUser(oAuthProviders.getVid());

                         oPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> tasksave) {
                                 if(tasksave.isSuccessful()){
                                     Toast.makeText(PostActivity.this,"la informacion se almaceno",Toast.LENGTH_SHORT).show();
                                 }else{
                                     Toast.makeText(PostActivity.this,"Error al guardar la informacion",Toast.LENGTH_SHORT).show();
                                 }
                             }
                         });

                     }
                 });
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