package com.thattechyguy.personalattendancemanager;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class firebaseManage {
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebaseDatabase;

    public firebaseManage() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void getScheduleData(String uid, ArraylistHashMapCallback myCallback){

        DatabaseReference loc = firebaseDatabase.getReference("attendance/"+uid+"/");


        Query query = loc.orderByChild("timestamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HashMap<String, Object>> scheduleMetaData = new ArrayList<HashMap<String, Object>>();
                for (DataSnapshot scheduleSnapshot: snapshot.getChildren()){
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    item.put("uniqueId", scheduleSnapshot.getKey());
//                    Log.d("harsh", String.valueOf(scheduleSnapshot.child("days").getValue()));
                    item.put("scheduleName", String.valueOf(scheduleSnapshot.child("Name").getValue()));
                    item.put("scheduleDescription", String.valueOf(scheduleSnapshot.child("description").getValue()));
                    item.put("numAttended", String.valueOf(scheduleSnapshot.child("attended").getValue()));
                    item.put("numTotal", String.valueOf(scheduleSnapshot.child("total").getValue()));

                    scheduleMetaData.add(item);
                }
                Collections.reverse(scheduleMetaData);
                myCallback.onCallback(scheduleMetaData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("harsh", "Error fetching data");
            }
        });
    }
    public void addSchedule(String uid, String scheduleName, String scheduleDescription, HashMap<String, Object> classes, intSuccessCallback myCallback){
        mDatabase = firebaseDatabase.getReference("attendance");

        String uniqueId = mDatabase.child(uid).push().getKey();

        HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("Name", scheduleName);
        metaData.put("description", scheduleDescription);
//        metaData.put("classes", classesList);
//        metaData.put("days", daysSelected);
        metaData.put("dailyClasses", classes);
        metaData.put("attended", "0");
        metaData.put("total", "0");
        metaData.put("timestamp", ServerValue.TIMESTAMP);
        String classUniqueId = mDatabase.child(uid).child(uniqueId).push().getKey();
        HashMap<String, Object> data = new HashMap<>();
        data.put("day", "Thursday");
        data.put("date", "01022024");
        data.put("attended", 0);
        data.put("total", 1);
        metaData.put(classUniqueId, data);

        mDatabase.child(uid).child(uniqueId).setValue(metaData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    myCallback.onCallback(1);
                }else {
                    myCallback.onCallback(0);
                }
            }
        });
    }
    public void getClassData(String path, ArraylistHashMapCallback myCallback){
        DatabaseReference loc = firebaseDatabase.getReference(path);

        Query query = loc.orderByChild("timestamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<HashMap<String, Object>> classesData = new ArrayList<HashMap<String, Object>>();
                for (DataSnapshot scheduleSnapshot: snapshot.getChildren()){
                    HashMap<String, Object> item = new HashMap<String, Object>();
                    if (scheduleSnapshot.hasChildren()) {
//                        Log.d("harsh", scheduleSnapshot.getKey());
                        if(scheduleSnapshot.getKey().equals("dailyClasses")){
                            item.put(scheduleSnapshot.getKey(), scheduleSnapshot.getValue());
                            classesData.add(item);
                            continue;
                        }
                        item.put("uniqueClassId", scheduleSnapshot.getKey());
                        item.put("date", scheduleSnapshot.child("date").getValue());
                        item.put("day", scheduleSnapshot.child("day").getValue());
                        item.put("numAttended", scheduleSnapshot.child("numAttended").getValue());
                        item.put("numTotal", scheduleSnapshot.child("numTotal").getValue());
                        item.put("totalClasses", scheduleSnapshot.child("totalClasses").getValue());
                        item.put("attendedClasses", scheduleSnapshot.child("attendedClasses").getValue());
                        item.put("holiday", scheduleSnapshot.child("holiday").getValue());
                        item.put("absent", scheduleSnapshot.child("absent").getValue());

                        classesData.add(item);
                    }

                }
                Collections.reverse(classesData);
//                Log.d("harsh", String.valueOf(classesData));
                myCallback.onCallback(classesData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("harsh", "Error fetching data");
            }
        });
    }
    public void updateClassData(String path, ArrayList<String> updatedAttendance, boolean holiday, boolean absent, intSuccessCallback myCallback){
        DatabaseReference loc = firebaseDatabase.getReference(path);

        HashMap<String, Object> data = new HashMap<>();
        data.put("attendedClasses", updatedAttendance);
        data.put("holiday", holiday);
        data.put("absent", absent);

        loc.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    myCallback.onCallback(1);
                }else{
                    Log.d("harsh", "Error while updating");
                    myCallback.onCallback(0);
                }
            }
        });
    }
}
