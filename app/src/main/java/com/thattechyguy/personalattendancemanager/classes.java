package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;

import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

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

        expandableClasses.setIndicatorBounds(50, 0);

        Intent i = getIntent();
        String path = i.getStringExtra("path");

        firebaseManage firebase = new firebaseManage();

        try{
            firebase.getLastAddedDate(path, new intSuccessCallback() {
                @Override
                public void onCallback(int success) {
                    firebase.getClassData(path, new ArraylistHashMapCallback() {
                        @Override
                        public void onCallback(ArrayList<HashMap<String, Object>> data) {
                            ArrayList<HashMap<String, Object>> classDays = new ArrayList<HashMap<String, Object>>();
                            ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();
//                Log.d("harsh", String.valueOf(data));
                            for(HashMap<String, Object> item:data){
//                        Log.d("harsh", String.valueOf(item));
                                if (!item.containsKey("dailyClasses")){
                                    dataList.add(item);
                                }
                            }
                            expandableClassesAdapter = new expandableClassesAdapter(classes.this, dataList, path);
                            expandableClasses.setAdapter(expandableClassesAdapter);
//                Log.d("harsh", String.valueOf(data));
                        }
                    });
                }
            });
        }catch(Exception e){
            Log.d("harsh", e.getMessage());
        }
    }
}