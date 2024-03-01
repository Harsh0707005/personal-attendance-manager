package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddSchedule extends AppCompatActivity {

    private EditText scheduleNameEdittext, scheduleDescriptionEdittext, classesNamesEdittext;
    private Button addSchedule;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        scheduleNameEdittext = findViewById(R.id.scheduleNameEdittext);
        scheduleDescriptionEdittext = findViewById(R.id.scheduleDescriptionEdittext);
        classesNamesEdittext = findViewById(R.id.classesNamesEdittext);
        addSchedule = findViewById(R.id.addSchedule);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        firebaseManage firebase = new firebaseManage();

        int[] daysButtonIds = {
                R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday,
                R.id.friday, R.id.saturday, R.id.sunday
        };
//        HashMap<String, Integer> daysSelected = new HashMap<String, Integer>();
        ArrayList<String> daysSelected = new ArrayList<>();

        for (int buttonId : daysButtonIds) {
            Button dayButton = findViewById(buttonId);
//            daysSelected.put(String.valueOf(buttonId), 0);
            dayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    String clickedId = String.valueOf(dayButton.getId());
                    String day = String.valueOf(dayButton.getText());
                    if(daysSelected.contains(day)){
                        daysSelected.remove(day);
                        dayButton.setBackgroundResource(R.drawable.rounded_blue_border_button);
                        dayButton.setTextColor(Color.BLACK);
                    }else{
                        daysSelected.add(day);
                        dayButton.setBackgroundResource(R.drawable.rounded_blue_button);
                        dayButton.setTextColor(Color.WHITE);
                    }

//                    if (daysSelected.get(String.valueOf(dayButton.getId())).equals(0)) {
//                        // Change the background to R.drawable.rounded_selected_button
//                        dayButton.setBackgroundResource(R.drawable.rounded_blue_button);
//                        daysSelected.replace(String.valueOf(dayButton.getId()), 1);
//                        dayButton.setTextColor(Color.WHITE);
//                    } else {
//                        dayButton.setBackgroundResource(R.drawable.rounded_blue_border_button);
//                        daysSelected.replace(String.valueOf(dayButton.getId()), 0);
//                        dayButton.setTextColor(Color.BLACK);
//
//                    }
//                    Log.d("harsh", String.valueOf(daysSelected));
                }
            });
        }


        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scheduleName = scheduleNameEdittext.getText().toString();
                String scheduleDescription = scheduleDescriptionEdittext.getText().toString();
                String classesNames = classesNamesEdittext.getText().toString();

                List<String> classesList = Arrays.asList(classesNames.split("\\s*,\\s*"));

                if(!scheduleName.isEmpty() && !classesNames.isEmpty() && !daysSelected.isEmpty()){
                    firebase.addSchedule(uid, scheduleName, scheduleDescription, classesList, daysSelected, new intSuccessCallback() {
                        @Override
                        public void onCallback(int success) {
                            if (success==1){
                                Toast.makeText(AddSchedule.this, "Schedule Added Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(AddSchedule.this, "Error Adding Schedule", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AddSchedule.this, "Schedule Name cannot be Empty!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}