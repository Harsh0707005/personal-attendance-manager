package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

public class AddSchedule extends AppCompatActivity {

    private EditText scheduleNameEdittext, scheduleDescriptionEdittext;
    private Button addSchedule;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        scheduleNameEdittext = findViewById(R.id.scheduleNameEdittext);
        scheduleDescriptionEdittext = findViewById(R.id.scheduleDescriptionEdittext);
        addSchedule = findViewById(R.id.addSchedule);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        firebaseManage firebase = new firebaseManage();

        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scheduleName = scheduleNameEdittext.getText().toString();
                String scheduleDesciption = scheduleDescriptionEdittext.getText().toString();

                if(!scheduleName.isEmpty()){
                    firebase.addSchedule(uid, scheduleName, scheduleDesciption, new intSuccessCallback() {
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