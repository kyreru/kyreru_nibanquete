package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.fracment.ChatFragment;
import com.example.myapplication.fracment.FilterFragment;
import com.example.myapplication.fracment.HomeFragment;
import com.example.myapplication.fracment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigation=findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment( new HomeFragment ());
    }
    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId()==R.id.itemHome){
                        openFragment(new HomeFragment());
                    }else if (item.getItemId()==R.id.itemChat){
                        openFragment(new ChatFragment());
                    }else if (item.getItemId()==R.id.itemFilter){
                        openFragment(new FilterFragment());
                    }else if (item.getItemId()==R.id.itemPerfil){
                        openFragment(new ProfileFragment());

                    }
                    return false;
                }
            };

}