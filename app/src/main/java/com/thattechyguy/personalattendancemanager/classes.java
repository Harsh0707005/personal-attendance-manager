package com.thattechyguy.personalattendancemanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.stringCallback;

import java.util.ArrayList;
import java.util.HashMap;

public class classes extends AppCompatActivity {
    private ExpandableListView expandableClasses;
    private expandableClassesAdapter expandableClassesAdapter;
    private TextView nextDateTextview;
    private LinearLayout nextDateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes);

        expandableClasses = findViewById(R.id.expandableClasses);
        nextDateLayout = findViewById(R.id.nextDateLayout);
        nextDateTextview = findViewById(R.id.nextDateTextview);

        expandableClasses.setIndicatorBounds(50, 0);

        Intent i = getIntent();
        String path = i.getStringExtra("path");

        firebaseManage firebase = new firebaseManage();

        try{
            firebase.getLastAddedDate(path, new stringCallback() {
                @Override
                public void onCallback(String nextDate) {
//                    Log.d("harsh", String.valueOf(success));
//                    Toast.makeText(classes.this, nextDate, Toast.LENGTH_SHORT).show();
                    firebase.getClassData(path, new ArraylistHashMapCallback() {
                        @Override
                        public void onCallback(ArrayList<HashMap<String, Object>> data) {

                            ArrayList<HashMap<String, Object>> dataList = new ArrayList<>();

//                            Log.d("harsh", String.valueOf(data));

                            for(HashMap<String, Object> item:data){
//                                Log.d("harsh", String.valueOf(item));
                                if (!item.containsKey("dailyClasses")){
                                    dataList.add(item);
                                }
                            }
                            expandableClassesAdapter = new expandableClassesAdapter(classes.this, dataList, path);
                            expandableClasses.setAdapter(expandableClassesAdapter);
                            nextDateLayout.setVisibility(View.VISIBLE);
                            nextDateTextview.setText(nextDate);
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