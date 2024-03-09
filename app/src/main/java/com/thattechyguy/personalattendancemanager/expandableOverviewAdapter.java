package com.thattechyguy.personalattendancemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class expandableOverviewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private HashMap<String, Object> attendance;
    private firebaseManage firebase;
    public expandableOverviewAdapter(Context context, HashMap<String, Object> attendance){
        this.context = context;
        this.attendance = attendance;

        firebase = new firebaseManage();
    }
    @Override
    public int getGroupCount() {
        return ((HashMap<String, Integer>) this.attendance.get("attended")).keySet().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ((HashMap<String, Integer>) this.attendance.get("attended")).keySet().toArray()[groupPosition];
    }

    @Override
    public HashMap<String, Integer> getChild(int groupPosition, int childPosition) {
        HashMap<String, Integer> child = new HashMap<>();

        child.put("numAttended", ((HashMap<String, Integer>) this.attendance.get("attended")).get(getGroup(groupPosition)));
        child.put("numTotal", ((HashMap<String, Integer>) this.attendance.get("total")).get(getGroup(groupPosition)));

        return child;
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
            convertView = inflater.inflate(R.layout.class_overview_title, null);
        }

        TextView classTitle = convertView.findViewById(R.id.classTitle);
        TextView numAttendedTextView = convertView.findViewById(R.id.numAttendedTextView);
        TextView numTotalTextView = convertView.findViewById(R.id.numTotalTextView);
        TextView numPercentTextView = convertView.findViewById(R.id.numPercentTextView);

        try{
            String className = getGroup(groupPosition).toString();
            Integer numAttended = ((HashMap<String, Integer>) this.attendance.get("attended")).get(className);
            Integer numTotal = ((HashMap<String, Integer>) this.attendance.get("total")).get(className);
            String percent = String.valueOf(((numAttended*100)/numTotal));

            classTitle.setText(className);
            numAttendedTextView.setText(numAttended.toString());
            numTotalTextView.setText(numTotal.toString());
            numPercentTextView.setText(percent+"%");
        }catch(Exception e){
            Log.d("harsh", e.getMessage());
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
