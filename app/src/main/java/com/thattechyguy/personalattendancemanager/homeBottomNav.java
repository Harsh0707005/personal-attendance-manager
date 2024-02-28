package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homeBottomNav extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_bottom_nav);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setItemIconTintList(null);

    }
}