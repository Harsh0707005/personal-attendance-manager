package com.thattechyguy.personalattendancemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.util.ArrayList;
import java.util.HashMap;

public class expandableClassesAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<HashMap<String, Object>> classesData;

    public expandableClassesAdapter(Context context, ArrayList<HashMap<String, Object>> classesData){
        this.context = context;
        this.classesData = classesData;
    }
    @Override
    public int getGroupCount() {
        return this.classesData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public HashMap<String, Object> getGroup(int groupPosition) {
        return this.classesData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
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
        try{
            date.setText(convertToDate(getGroup(groupPosition).get("date").toString()));
            day.setText(getGroup(groupPosition).get("day").toString());
            numAttended.setText("Attended: " + getGroup(groupPosition).get("numAttended").toString());
            numTotal.setText("Total: " + getGroup(groupPosition).get("numTotal").toString());
        }catch(Exception e){
            Log.d("harsh", e.getMessage());
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_data, null);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public String convertToDate(String input) {
        SimpleDateFormat sdfInput = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
        SimpleDateFormat sdfOutput = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
        try {
            Date date = sdfInput.parse(input);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            return day + " " + new DateFormatSymbols().getMonths()[month - 1] + " " + year;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
