package com.example.myapplication.providers;

import android.content.Context;

import com.example.myapplication.utils.CompressorBitmapImage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

public class ImageProviders {
    StorageReference oStorage;

    public ImageProviders() {
        oStorage= FirebaseStorage.getInstance().getReference();

    }
    public UploadTask save(Context context, File file){
        byte[] imageByte= CompressorBitmapImage.getImage(context,file.getPath(), 500,500);
        StorageReference storage= oStorage.child(new Date()+"jpg");
        UploadTask task=storage.putBytes(imageByte);
        oStorage=storage;
        return task;

    }
    public StorageReference getoStorage(){
        return oStorage;
    }
}
