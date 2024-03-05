package com.thattechyguy.personalattendancemanager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;
import java.util.HashMap;

public class expandableClassesAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> classesData;
    private ArrayList<String> attendedClasses;
    private firebaseManage firebase;
    private String path;

    public expandableClassesAdapter(Context context, ArrayList<HashMap<String, Object>> classesData, String path){
        this.context = context;
        this.classesData = classesData;
        this.path = path;

        firebase = new firebaseManage();
    }
    @Override
    public int getGroupCount() {
        return this.classesData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public HashMap<String, Object> getGroup(int groupPosition) {
        return this.classesData.get(groupPosition);
    }

    @Override
    public ArrayList<String> getChild(int groupPosition, int childPosition) {
        return (ArrayList<String>) this.classesData.get(groupPosition).get("totalClasses");
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_title, null);
        }
        TextView date = convertView.findViewById(R.id.date);
        TextView day = convertView.findViewById(R.id.day);
        TextView numAttended = convertView.findViewById(R.id.numAttended);
        TextView numTotal = convertView.findViewById(R.id.numTotal);
        ImageView markedIcon = convertView.findViewById(R.id.markedIcon);
        try{
            date.setText(convertToDate(getGroup(groupPosition).get("date").toString()));
            day.setText(getGroup(groupPosition).get("day").toString());
            numAttended.setText("Attended: " + getGroup(groupPosition).get("numAttended").toString());
            numTotal.setText("Total: " + getGroup(groupPosition).get("numTotal").toString());
            if (getGroup(groupPosition).get("marked").toString().equals("true")){
                markedIcon.setVisibility(View.VISIBLE);
            }else {
                markedIcon.setVisibility(View.GONE);
            }
        }catch(Exception e){
            Log.d("harsh", e.getMessage());
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ArrayList<String> classes = getChild(groupPosition, childPosition);
//        Log.d("harsh", String.valueOf(classes));
//        Log.d("harsh", String.valueOf(getGroup(groupPosition)));
        HashMap<String, Object> group = getGroup(groupPosition);

        attendedClasses = (ArrayList<String>) getGroup(groupPosition).get("attendedClasses");

        if (attendedClasses==null){
            attendedClasses = new ArrayList<String>();
        }
//        Log.d("harsh", String.valueOf(attendedClasses));

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_data, null);
        }

        LinearLayout classesLayout = convertView.findViewById(R.id.classesLayout);

        Button holidayBtn = convertView.findViewById(R.id.holidayBtn);
        final boolean[] holiday = {false};
        Button absentBtn = convertView.findViewById(R.id.absentBtn);
        final boolean[] absent = {false};
        Button updateBtn = convertView.findViewById(R.id.updateBtn);

        holidayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absent[0] = unselectButton(absentBtn);

                attendedClasses.clear();

                unselectButtonLayout(classesLayout);

                if (holiday[0]){
                    holiday[0] = unselectButton(holidayBtn);

                    enableClassesLayout(classesLayout);
                }else{
                    holiday[0] = selectButton(holidayBtn);

                    disableClassesLayout(classesLayout);
                }
            }
        });

        absentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holiday[0] = unselectButton(holidayBtn);

                attendedClasses.clear();

                unselectButtonLayout(classesLayout);

                if (absent[0]){
                    absent[0] = unselectButton(absentBtn);

                    enableClassesLayout(classesLayout);
                }else{
                    absent[0] = selectButton(absentBtn);

                    absentBtn.setBackgroundResource(R.drawable.rounded_blue_button);
                    absentBtn.setTextColor(Color.WHITE);
                    absent[0] = true;

                    disableClassesLayout(classesLayout);
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase.updateClassData(path + getGroup(groupPosition).get("uniqueClassId").toString(), group, attendedClasses, classes, holiday[0], absent[0], new intSuccessCallback() {
                    @Override
                    public void onCallback(int success) {
                        if (success==1){
                            Toast.makeText(context, "Updated Successfully!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        classesLayout.removeAllViews();

        ArrayList<Integer> buttonIds = new ArrayList<Integer>();

        try{
            for (String className: classes) {
                View view = LayoutInflater.from(context).inflate(R.layout.classes_button, null);
                Button button = view.findViewById(R.id.classNameBtn);
                button.setText(className.toString());
                int id = View.generateViewId();
                button.setId(id);
                buttonIds.add(id);

                if (attendedClasses.contains(className.toString())){
                    selectButton(button);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = button.getText().toString();
                        if (attendedClasses.contains(text)){
                            attendedClasses.remove(text);
                            unselectButton(button);
                        }else {
                            attendedClasses.add(text);
                            selectButton(button);
                        }
                    }
                });
                
                classesLayout.addView(view);
            }
        }catch(Exception e){
            Log.d("harsh", e.getMessage());
        }

        if (group.get("holiday").equals(true)){
            holiday[0] = selectButton(holidayBtn);
            disableClassesLayout(classesLayout);
        } else if (group.get("absent").equals(true)) {
            absent[0] = selectButton(absentBtn);
            disableClassesLayout(classesLayout);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private boolean selectButton(Button button){
        button.setBackgroundResource(R.drawable.rounded_blue_button);
        button.setTextColor(Color.WHITE);
        return true;
    }
    private boolean unselectButton(Button button){
        button.setBackgroundResource(R.drawable.rounded_blue_border_button);
        button.setTextColor(Color.BLACK);
        return false;
    }
    private void unselectButtonLayout(LinearLayout layout){
        for ( int i = 0; i < layout.getChildCount();  i++ ){
            Button button = (Button) layout.getChildAt(i);
            button.setBackgroundResource(R.drawable.rounded_blue_border_button);
            button.setTextColor(Color.BLACK);
        }
    }

    private void disableClassesLayout(LinearLayout layout){
        for ( int i = 0; i < layout.getChildCount();  i++ ){
            Button view = (Button) layout.getChildAt(i);
            view.setBackgroundResource(R.drawable.rounded_disable_button);
            view.setTextColor(ContextCompat.getColor(context, R.color.gray));
            view.setEnabled(false);
        }
    }
    private void enableClassesLayout(LinearLayout layout){
        for ( int i = 0; i < layout.getChildCount();  i++ ){
            Button view = (Button) layout.getChildAt(i);
            view.setBackgroundResource(R.drawable.rounded_blue_border_button);
            view.setTextColor(ContextCompat.getColor(context, R.color.black));
            view.setEnabled(true);
        }
    }
    public String convertToDate(String input) {

        try {
            SimpleDateFormat sdfInput = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
            SimpleDateFormat sdfOutput = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
            Date date = sdfInput.parse(input);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            return day + " " + new DateFormatSymbols().getMonths()[month - 1] + " " + year;
        } catch (Exception e) {
            Log.d("harsh", e.getMessage());
            return null;
        }
    }
}
