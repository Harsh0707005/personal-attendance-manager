package com.thattechyguy.personalattendancemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddSchedule extends AppCompatActivity {

    private EditText scheduleNameEdittext, scheduleDescriptionEdittext, classesNamesEdittext;
    private Button addSchedule, addDay;
    private LinearLayout classesLayout;
    private Spinner daysSpinner;
    private ArrayAdapter<String> adapter;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        scheduleNameEdittext = findViewById(R.id.scheduleNameEdittext);
        scheduleDescriptionEdittext = findViewById(R.id.scheduleDescriptionEdittext);
        classesNamesEdittext = findViewById(R.id.classesNamesEdittext);
        addDay = findViewById(R.id.addDay);
        addSchedule = findViewById(R.id.addSchedule);
        daysSpinner = findViewById(R.id.daysSpinner);
        classesLayout = findViewById(R.id.classesLayout);

        String[] daysStringArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        List<String> daysArray = new ArrayList<String>(Arrays.asList(daysStringArray));

        HashMap<String, Object> classes = new HashMap<String, Object>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, daysArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daysSpinner.setAdapter(adapter);


        final int[] currentSpinner = {R.id.daysSpinner};
        final int[] currentEdittext = {R.id.classesNamesEdittext};

        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!((EditText) findViewById(currentEdittext[0])).getText().toString().isEmpty()){
                    List<String> classesList = Arrays.asList(((EditText) findViewById(currentEdittext[0])).getText().toString().split("\\s*,\\s*"));

                    String selectedDay = ((Spinner) findViewById(currentSpinner[0])).getSelectedItem().toString();

                    classes.put(selectedDay, classesList);


                    daysArray.remove(selectedDay);

//                    Log.d("harsh", String.valueOf(classes));
                    if (daysArray.isEmpty()){
                        ((Spinner) findViewById(currentSpinner[0])).setEnabled(false);
                        ((EditText) findViewById(currentEdittext[0])).setEnabled(false);
                        addDay.setVisibility(View.GONE);
                        return;
                    }

                    View view = LayoutInflater.from(AddSchedule.this).inflate(R.layout.add_class_day_layout, null);

                    Spinner spinner = view.findViewById(R.id.spinner);
                    spinner.setId(View.generateViewId());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    EditText editText = view.findViewById(R.id.edittext);
                    editText.setId(View.generateViewId());

                    classesLayout.addView(view);

                    ((Spinner) findViewById(currentSpinner[0])).setEnabled(false);
                    ((EditText) findViewById(currentEdittext[0])).setEnabled(false);

                    currentEdittext[0] = editText.getId();
                    currentSpinner[0] = spinner.getId();

                }else{
                    Toast.makeText(AddSchedule.this, "Classes cannot be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        firebaseManage firebase = new firebaseManage();

//        int[] daysButtonIds = {
//                R.id.monday, R.id.tuesday, R.id.wednesday, R.id.thursday,
//                R.id.friday, R.id.saturday, R.id.sunday
//        };
////        HashMap<String, Integer> daysSelected = new HashMap<String, Integer>();
//        ArrayList<String> daysSelected = new ArrayList<>();
//
//        for (int buttonId : daysButtonIds) {
//            Button dayButton = findViewById(buttonId);
////            daysSelected.put(String.valueOf(buttonId), 0);
//            dayButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    String clickedId = String.valueOf(dayButton.getId());
//                    String day = String.valueOf(dayButton.getText());
//                    if(daysSelected.contains(day)){
//                        daysSelected.remove(day);
//                        dayButton.setBackgroundResource(R.drawable.rounded_blue_border_button);
//                        dayButton.setTextColor(Color.BLACK);
//                    }else{
//                        daysSelected.add(day);
//                        dayButton.setBackgroundResource(R.drawable.rounded_blue_button);
//                        dayButton.setTextColor(Color.WHITE);
//                    }
//
////                    if (daysSelected.get(String.valueOf(dayButton.getId())).equals(0)) {
////                        // Change the background to R.drawable.rounded_selected_button
////                        dayButton.setBackgroundResource(R.drawable.rounded_blue_button);
////                        daysSelected.replace(String.valueOf(dayButton.getId()), 1);
////                        dayButton.setTextColor(Color.WHITE);
////                    } else {
////                        dayButton.setBackgroundResource(R.drawable.rounded_blue_border_button);
////                        daysSelected.replace(String.valueOf(dayButton.getId()), 0);
////                        dayButton.setTextColor(Color.BLACK);
////
////                    }
////                    Log.d("harsh", String.valueOf(daysSelected));
//                }
//            });
//        }


        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String scheduleName = scheduleNameEdittext.getText().toString();
                    String scheduleDescription = scheduleDescriptionEdittext.getText().toString();
                    String classesNames = classesNamesEdittext.getText().toString();

                    List<String> classesList = Arrays.asList(classesNames.split("\\s*,\\s*"));

                    List<String> tempClasses = Arrays.asList(((EditText) findViewById(currentEdittext[0])).getText().toString().split("\\s*,\\s*"));
                    String tempDay = ((Spinner) findViewById(currentSpinner[0])).getSelectedItem().toString();

                    if(!tempClasses.isEmpty() && !classes.containsKey(tempDay)){
//                        Log.d("harsh", tempDay + " " + tempClasses + " " + classes + " " + classesNames + " " + scheduleName);
                        classes.put(tempDay, tempClasses);
                    }

                    if(!scheduleName.isEmpty() && !classesNames.isEmpty() && !classes.isEmpty()){
                        firebase.addSchedule(uid, scheduleName, scheduleDescription, classes, new intSuccessCallback() {
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
                        Toast.makeText(AddSchedule.this, "Schedule Data cannot be Empty!!!", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    Log.d("harsh", "add " + e.getMessage());
                }
            }
        });

    }
}