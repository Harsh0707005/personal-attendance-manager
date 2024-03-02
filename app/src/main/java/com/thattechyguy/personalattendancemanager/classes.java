package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;

import java.util.ArrayList;
import java.util.HashMap;

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

        firebase.getClassData(path, new ArraylistHashMapCallback() {
            @Override
            public void onCallback(ArrayList<HashMap<String, Object>> data) {
                Log.d("harsh", String.valueOf(data));
            }
        });

        expandableClassesAdapter = new expandableClassesAdapter(this);
        expandableClasses.setAdapter(expandableClassesAdapter);
    }
}