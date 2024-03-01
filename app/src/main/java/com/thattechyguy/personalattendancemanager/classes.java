package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ExpandableListView;

public class classes extends AppCompatActivity {
    private ExpandableListView expandableClasses;
    expandableClassesAdapter expandableClassesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        expandableClasses = findViewById(R.id.expandableClasses);

        Intent i = getIntent();
        String path = i.getStringExtra("path");

        firebaseManage firebase = new firebaseManage();

        firebase.getClassData(path);

        expandableClassesAdapter = new expandableClassesAdapter(this);
        expandableClasses.setAdapter(expandableClassesAdapter);
    }
}