package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;

import java.util.ArrayList;

public class samplePush extends AppCompatActivity {
    private DatabaseReference attendanceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_push);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        attendanceRef = database.getReference("attendance");

        // Sample data for user ID: iUXcZmmL2nZvZvFBIwMXG3hm2VP2
        String userId = "iUXcZmmL2nZvZvFBIwMXG3hm2VP2";

        // Sample data for schedule 1
//        String scheduleKey1 = attendanceRef.child(userId).push().getKey(); // Generate unique key for schedule
        String scheduleKey1 = "-NruyaZi2tZBSt3FF2RT";
        String scheduleName1 = "School";
        long timestamp1 = System.currentTimeMillis();
        int totalClasses1 = 20;
        int attendedClasses1 = 15;

        // Push schedule 1 data to Firebase
//        attendanceRef.child(userId).child(scheduleKey1).child("Name").setValue(scheduleName1);
//        attendanceRef.child(userId).child(scheduleKey1).child("timestamp").setValue(timestamp1);
//        attendanceRef.child(userId).child(scheduleKey1).child("total").setValue(totalClasses1);
//        attendanceRef.child(userId).child(scheduleKey1).child("attended").setValue(attendedClasses1);

        // Sample data for day 1 of schedule 1
//        String dayKey1_1 = attendanceRef.child(userId).child(scheduleKey1).push().getKey(); // Generate unique key for day
//        long timestampDay1_1 = timestamp1; // Change timestamp for day 1 if needed
//        int totalClassesDay1_1 = 20;
//        int attendedClassesDay1_1 = 10;
//        String day = "Thursday";
//        String date = "10 February, 2024";
//
//        // Push day 1 data of schedule 1 to Firebase
//        attendanceRef.child(userId).child(scheduleKey1).child(dayKey1_1).child("timestamp").setValue(timestampDay1_1);
//        attendanceRef.child(userId).child(scheduleKey1).child(dayKey1_1).child("total").setValue(totalClassesDay1_1);
//        attendanceRef.child(userId).child(scheduleKey1).child(dayKey1_1).child("attended").setValue(attendedClassesDay1_1);
//        attendanceRef.child(userId).child(scheduleKey1).child(dayKey1_1).child("day").setValue(day);
//        attendanceRef.child(userId).child(scheduleKey1).child(dayKey1_1).child("date").setValue(date);

        ArrayList<String> item = new ArrayList<>();
        item.add("abc");
        item.add("chem");

        attendanceRef.child(userId).child("-Ns-T8wOOHI8ED4km4Rx").child("-Ns-T8wPOUGwtojrzAe-").child("totalClasses").setValue(item);
    }
}