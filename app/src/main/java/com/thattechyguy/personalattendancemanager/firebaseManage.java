package com.thattechyguy.personalattendancemanager;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
                ArrayList<HashMap<String, String>> scheduleMetaData = new ArrayList<HashMap<String, String>>();
                for (DataSnapshot scheduleSnapshot: snapshot.getChildren()){
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("uniqueId", scheduleSnapshot.getKey());
                    item.put("scheduleName", String.valueOf(scheduleSnapshot.child("Name").getValue()));
                    item.put("numAttended", String.valueOf(scheduleSnapshot.child("attended").getValue()));
                    item.put("numTotal", String.valueOf(scheduleSnapshot.child("total").getValue()));

                    scheduleMetaData.add(item);
                }
                myCallback.onCallback(scheduleMetaData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("harsh", "Error fetching data");
            }
        });
    }
}
