package com.thattechyguy.personalattendancemanager;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.HashMapObjectCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.booleanSuccessCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.stringCallback;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
                ArrayList<HashMap<String, Object>> scheduleMetaData = new ArrayList<>();
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
    public void addSchedule(String uid, String scheduleName, String scheduleDescription, String startDate, HashMap<String, Object> classes, intSuccessCallback myCallback){
        mDatabase = firebaseDatabase.getReference("attendance");


        String uniqueId = mDatabase.child(uid).push().getKey();

        HashMap<String, Object> metaData = new HashMap<>();
        metaData.put("Name", scheduleName);
        metaData.put("description", scheduleDescription);
        metaData.put("startDate", startDate);
        metaData.put("dailyClasses", classes);
        metaData.put("attended", 0);
        metaData.put("total", 0);
        metaData.put("timestamp", ServerValue.TIMESTAMP);

        String classUniqueId = mDatabase.child(uid).child(uniqueId).push().getKey();

        // TODO: Change this to today's date and data

        HashMap<String, Object> data = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        Date rawDate = calendar.getTime();

        int numDay = calendar.get(Calendar.DAY_OF_WEEK);
        String day;

        switch (numDay) {
            case Calendar.SUNDAY:
                day = "Sunday";
                break;
            case Calendar.MONDAY:
                day = "Monday";
                break;
            case Calendar.TUESDAY:
                day = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                day = "Wednesday";
                break;
            case Calendar.THURSDAY:
                day = "Thursday";
                break;
            case Calendar.FRIDAY:
                day = "Friday";
                break;
            case Calendar.SATURDAY:
                day = "Saturday";
                break;
            default:
                day = "Unknown";
        }

        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        String date = df.format(rawDate);

        try{
            data.put("day", day);
            data.put("date", date);
            data.put("numAttended", 0);
            data.put("numTotal", ((List<String>) classes.get(day)).size());
            data.put("absent", false);
            data.put("holiday", false);
            data.put("marked", false);
            data.put("totalClasses", classes.get(day));
            data.put("timestamp", calendar.getTimeInMillis());

            metaData.put(classUniqueId, data);
        }catch(Exception e){
            Log.d("harsh", "no class to add");
        }

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
                        item.put("marked", scheduleSnapshot.child("marked").getValue());

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
    public void updateClassData(String path, HashMap<String, Object> initialAttendance, ArrayList<String> updatedAttendance, ArrayList<String> totalClasses, boolean holiday, boolean absent, intSuccessCallback myCallback){
        DatabaseReference loc = firebaseDatabase.getReference(path);

        HashMap<String, Object> data = new HashMap<>();
        data.put("attendedClasses", updatedAttendance);
        data.put("holiday", holiday);
        data.put("absent", absent);
        if (holiday){
            data.put("numAttended", 0);
            data.put("numTotal", 0);
        }else if (absent){
            data.put("numAttended", 0);
            data.put("numTotal", totalClasses.size());
        }else{
            data.put("numAttended", updatedAttendance.size());
            data.put("numTotal", totalClasses.size());
        }
        if (!holiday && !absent && updatedAttendance.size()==0){
            data.put("marked", false);
        }else{
            data.put("marked", true);
        }

        loc.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    HashMap<String, Object> item = new HashMap<String, Object>();

                    if (initialAttendance.get("holiday").toString().equals("false") && initialAttendance.get("absent").toString().equals("false") && ((ArrayList<String>) initialAttendance.get("attendedClasses"))==null && (updatedAttendance.size()!=0 || absent)){
                        item.put("total", ServerValue.increment(totalClasses.size()));
                    }

                    if (updatedAttendance.size()!=0){
                        if (initialAttendance.get("holiday").toString().equals("true")){
                            item.put("total", ServerValue.increment(totalClasses.size()));
                        }
                        item.put("attended", ServerValue.increment(updatedAttendance.size()- ((Long) initialAttendance.get("numAttended"))));
                    } else if (holiday && initialAttendance.get("holiday").toString().equals("false")) {
                        item.put("attended", ServerValue.increment(-((Long) initialAttendance.get("numAttended"))));
                        if (!(initialAttendance.get("holiday").toString().equals("false") && initialAttendance.get("absent").toString().equals("false") && ((ArrayList<String>) initialAttendance.get("attendedClasses"))==null)) {
                            item.put("total", ServerValue.increment(-totalClasses.size()));
                        }
                    } else if (absent) {
                        if (initialAttendance.get("holiday").toString().equals("true")){
                            item.put("total", ServerValue.increment(totalClasses.size()));
                        }
                        item.put("attended", ServerValue.increment(-((Long) initialAttendance.get("numAttended"))));
                    } else if (!holiday && !absent && updatedAttendance.size()==0 && initialAttendance.get("holiday").toString().equals("false") && (initialAttendance.get("absent").toString().equals("true") || ((ArrayList<String>) initialAttendance.get("attendedClasses"))!=null)) {
                        item.put("attended", ServerValue.increment(updatedAttendance.size()- ((Long) initialAttendance.get("numAttended"))));
                        item.put("total", ServerValue.increment(-totalClasses.size()));
                    }

                    loc.getParent().updateChildren(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            myCallback.onCallback(1);
                        }
                    });
                    myCallback.onCallback(1);
                }else{
                    Log.d("harsh", "Error while updating");
                    myCallback.onCallback(0);
                }
            }
        });
    }
    public void getLastAddedDate(String path, stringCallback myCallback) {

        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference(path);
        Query getDays = classRef.child("dailyClasses");

        ArrayList<String> days = new ArrayList<>();

        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Query lastQuery = classRef.orderByChild("timestamp").limitToLast(1);

                    DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);

                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                try{
//                                    Log.d("harsh", String.valueOf(dataSnapshot.getChildren().iterator().next().child("date").getValue()));
                                    String lastAddedDate = String.valueOf(dataSnapshot.getChildren().iterator().next().child("date").getValue());
//                                    Log.d("harsh", String.valueOf(lastAddedDate=="null"));

//                                    Log.d("harsh", String.valueOf(snapshot.getValue()));

                                    if (lastAddedDate.equals("null")){
//                                        myCallback.onCallback(0);

//                                        Log.d("harsh", timestampToDate((Long) ((HashMap<String, Object>) snapshot.getValue()).get("timestamp")));
//                                        lastAddedDate = timestampToDate((Long) ((HashMap<String, Object>) snapshot.getValue()).get("timestamp"));

                                        lastAddedDate = ((HashMap<String, Object>) snapshot.getValue()).get("startDate").toString();

                                        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);

                                        Date startDate = dateFormat.parse(lastAddedDate);

                                        Calendar calendar = Calendar.getInstance();

                                        calendar.setTime(startDate);
                                        calendar.add(Calendar.DATE, -1);

                                        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                                        lastAddedDate = df.format(calendar.getTime());

                                    }

//                                    String finalLastAddedDate = lastAddedDate;
                                    calculateDates(path, lastAddedDate, ((HashMap<String, Object>)((HashMap<String, Object>) snapshot.getValue()).get("dailyClasses")).keySet(), ((HashMap<String, Object>)((HashMap<String, Object>) snapshot.getValue()).get("dailyClasses")), new stringCallback() {
                                        @Override
                                        public void onCallback(String str_data) {
                                            calculateNextNearestDate(str_data, ((HashMap<String, Object>) ((HashMap<String, Object>) snapshot.getValue()).get("dailyClasses")).keySet(), new stringCallback() {
                                                @Override
                                                public void onCallback(String nextDate) {
                                                    myCallback.onCallback(nextDate);
                                                }
                                            });
//                                            myCallback.onCallback(1);
                                        }
                                    });
//                    callback.onCallback();
//                    callback.onCallback(convertToDate(lastAddedDate));
                                }catch(Exception e){
                                    Log.d("harsh", "last query " + e.getMessage());
                                }
                            } else {
                                Log.d("harsh", "no");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }else{
                    Log.d("harsh", "doesnt exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public ArrayList<HashMap<String, Object>> calculateDates(String path, String lastAddedDate, Set<String> days, HashMap<String, Object> classes, stringCallback myCallback) {
        ArrayList<HashMap<String, Object>> dateList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
        try{
            Date startDate = dateFormat.parse(lastAddedDate);

            Calendar calendar = Calendar.getInstance();
            Date rawDate = calendar.getTime();

            String stringDate = dateFormat.format(rawDate);

            Date todayDate = dateFormat.parse(stringDate);

            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);

            while (!calendar.getTime().after(todayDate)) {
                int numDay = calendar.get(Calendar.DAY_OF_WEEK);

                String calendarDay;

                switch (numDay) {
                    case Calendar.SUNDAY:
                        calendarDay = "Sunday";
                        break;
                    case Calendar.MONDAY:
                        calendarDay = "Monday";
                        break;
                    case Calendar.TUESDAY:
                        calendarDay = "Tuesday";
                        break;
                    case Calendar.WEDNESDAY:
                        calendarDay = "Wednesday";
                        break;
                    case Calendar.THURSDAY:
                        calendarDay = "Thursday";
                        break;
                    case Calendar.FRIDAY:
                        calendarDay = "Friday";
                        break;
                    case Calendar.SATURDAY:
                        calendarDay = "Saturday";
                        break;
                    default:
                        calendarDay = "Unknown";
                }

                if (days.contains(calendarDay)){
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("date", dateFormat.format(calendar.getTime()));
                    item.put("day", calendarDay);
                    item.put("timestamp", String.valueOf(calendar.getTimeInMillis()));

                    lastAddedDate = dateFormat.format(calendar.getTime());

                    dateList.add(item);
//                    Log.d("harsh", String.valueOf(dateList));
//                    dateList.add(dateFormat.format(calendar.getTime()));
                }
                calendar.add(Calendar.DATE, 1);
            }
        }catch(Exception e){
            Log.d("harsh", "calculate date " + e.getMessage());
        }
//        Log.d("harsh", String.valueOf(dateList));
        String finalLastAddedDate = lastAddedDate;
        pushClassData(path, dateList, classes, new intSuccessCallback() {
            @Override
            public void onCallback(int success) {
                if (success==1) {
//                    Log.d("harsh", "updated " + finalLastAddedDate);
                    myCallback.onCallback(finalLastAddedDate);
                }else{
                    myCallback.onCallback(finalLastAddedDate);
                }
            }
        });
        return dateList;
    }

    public void pushClassData(String path, ArrayList<HashMap<String, Object>> dateList, HashMap<String, Object> classes, intSuccessCallback myCallback) {
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference(path);
        if (dateList.isEmpty()){
            myCallback.onCallback(1);
        }
        for (HashMap<String, Object> item : dateList) {
            String uniqueClassId = classRef.push().getKey();
            HashMap<String, Object> data = new HashMap<>();
            data.put("day", item.get("day"));
            data.put("date", item.get("date"));
            data.put("timestamp", item.get("timestamp"));
            data.put("numAttended", 0);
            data.put("numTotal", ((List<String>) classes.get(item.get("day"))).size());
            data.put("absent", false);
            data.put("holiday", false);
            data.put("marked", false);
            data.put("totalClasses", classes.get(item.get("day")));

            classRef.child(uniqueClassId).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        myCallback.onCallback(1);
                    }else{
                        myCallback.onCallback(0);
                    }
                }
            });
        }
    }

    public void calculateNextNearestDate(String lastAddedDate, Set<String> days, stringCallback myCallback) {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
//        Log.d("harsh", lastAddedDate);
        try {
            Date startDate = dateFormat.parse(lastAddedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);

            Calendar todayCalendar = Calendar.getInstance();
            Date todayDate = new Date();
            String calendarDay = "";
//            while (!days.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
            while (!days.contains(calendarDay)) {
//                Log.d("harsh", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
                int numDay = calendar.get(Calendar.DAY_OF_WEEK);
                switch (numDay) {
                    case Calendar.SUNDAY:
                        calendarDay = "Sunday";
                        break;
                    case Calendar.MONDAY:
                        calendarDay = "Monday";
                        break;
                    case Calendar.TUESDAY:
                        calendarDay = "Tuesday";
                        break;
                    case Calendar.WEDNESDAY:
                        calendarDay = "Wednesday";
                        break;
                    case Calendar.THURSDAY:
                        calendarDay = "Thursday";
                        break;
                    case Calendar.FRIDAY:
                        calendarDay = "Friday";
                        break;
                    case Calendar.SATURDAY:
                        calendarDay = "Saturday";
                        break;
                    default:
                        calendarDay = "Unknown";
                }

                calendar.add(Calendar.DATE, 1);
            }
            calendar.add(Calendar.DATE, -1);

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

//            Log.d("harsh", "date " + day + " " + new DateFormatSymbols().getMonths()[month] + " " + year);
            myCallback.onCallback(day + " " + new DateFormatSymbols().getMonths()[month] + " " + year); // No suitable date found
        } catch (Exception e) {
            Log.d("harsh", "calculate next date " + e.getMessage());
            myCallback.onCallback("");
        }
    }

    public void getAttendanceDate(String path, HashMapObjectCallback myCallback){
        DatabaseReference loc = firebaseDatabase.getReference(path);

        loc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> attendance = new HashMap<>();

                attendance.put("total", new HashMap<>());
                attendance.put("attended", new HashMap<>());

                int numTotal = 0;
                int numAttended = 0;

                ArrayList<String> classes = new ArrayList<>();
                for (DataSnapshot scheduleSnapshot: snapshot.getChildren()){
                    if (scheduleSnapshot.hasChildren()) {
                        if(scheduleSnapshot.getKey().equals("dailyClasses")){
                            continue;
                        }
                        boolean holiday = (boolean) scheduleSnapshot.child("holiday").getValue();
                        boolean marked = (boolean) scheduleSnapshot.child("marked").getValue();

                        if (!holiday && marked){

                            for (String temp : (ArrayList<String>) scheduleSnapshot.child("totalClasses").getValue()){
                                if (!classes.contains(temp)){
                                    classes.add(temp);
                                }
                                try{
                                    int count = 0;
                                    if (((HashMap<String, Integer>) attendance.get("total")).containsKey(temp)) {
                                        count = ((HashMap<String, Integer>) attendance.get("total")).get(temp);
                                    }
                                    ((HashMap<String, Integer>) attendance.get("total")).put(temp, count + 1);
                                    if (!((HashMap<String, Integer>) attendance.get("attended")).containsKey(temp)) {
                                        ((HashMap<String, Integer>) attendance.get("attended")).put(temp, 0);
                                    }
                                    numTotal = numTotal + 1;

                                }catch(Exception e){
                                    Log.d("harsh", "total calculation " + e.getMessage());
                                }
                            }

                            try{
                                if ((ArrayList<String>) scheduleSnapshot.child("attendedClasses").getValue() == null){
                                    continue;
                                }
                                for (String temp : (ArrayList<String>) scheduleSnapshot.child("attendedClasses").getValue()){
                                    int count = 0;
                                    if (((HashMap<String, Integer>) attendance.get("attended")).containsKey(temp)) {
                                        count = ((HashMap<String, Integer>) attendance.get("attended")).get(temp);
                                    }
                                    ((HashMap<String, Integer>) attendance.get("attended")).put(temp, count+1);
                                    numAttended = numAttended + 1;
                                }

                            }catch(Exception e){
                                Log.d("harsh", "attended calculation " + e.getMessage());
                            }

                        }

                    }

                }

                attendance.put("numAttended", numAttended);
                attendance.put("numTotal", numTotal);
                attendance.put("classes", classes);

                myCallback.onCallback(attendance);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("harsh", "Error fetching data");
            }
        });
    }
    public void deleteSchedule(String path, booleanSuccessCallback myCallback){
        DatabaseReference loc = firebaseDatabase.getReference(path);

        loc.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    myCallback.onCallback(true);
                }else{
                    myCallback.onCallback(false);
                }
            }
        });
    }
    public String timestampToDate(long timestamp) {
        try {
            // Create a DateFormatter object with the desired format
            SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());

            // Create a calendar object and set the time in milliseconds
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            calendar.add(Calendar.DATE, -1);

            // Format the date using the formatter
            return formatter.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
