package com.example.myapplication.providers;

import com.example.myapplication.activities.PostActivity;
import com.example.myapplication.models.Post;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostProvider {

    CollectionReference oColletion;

    public PostProvider() {
        oColletion= FirebaseFirestore.getInstance().collection("Post");
    }
    public Task<Void> save(Post post){
        return oColletion.document().set(post);

    }
}
