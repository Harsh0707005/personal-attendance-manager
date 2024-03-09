package com.thattechyguy.personalattendancemanager;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.HashMapObjectCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        PieChart pieChart;
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ExpandableListView expandableListOverview = rootView.findViewById(R.id.expandableListOverview);

//        {attended={BEE=14, EFE=8, EM=10, Maths-2=14, Mech. Workshop=4, BEE Lab=5, BME=11, BME Lab=5, Physics Lab=5, EL & EC Workshop=3, Physics=9, EM Lab=3}, numTotal=171, total={BEE=24, EFE=15, EM=16, Maths-2=32, Mech. Workshop=8, BEE Lab=7, BME Lab=8, BME=17, Physics Lab=9, EL & EC Workshop=9, EM Lab=8, Physics=18}, numAttended=91, classes=[EM Lab, BEE, Maths-2, Mech. Workshop, BME Lab, EFE, BME, Physics, EL & EC Workshop, EM, Physics Lab, BEE Lab]}

//        pieChart = rootView.findViewById(R.id.pieChart_view);


        firebaseManage firebase = new firebaseManage();
        firebase.getAttendanceDate("attendance/iUXcZmmL2nZvZvFBIwMXG3hm2VP2/-NndQUc2kgcjJSH2-lw9", new HashMapObjectCallback() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
                Log.d("harsh", String.valueOf(data));
                try{
                expandableOverviewAdapter adapter = new expandableOverviewAdapter(rootView.getContext(), data);
                expandableListOverview.setAdapter(adapter);
                }catch(Exception e){
                    Log.d("harsh", e.getMessage());
                }
//                showPieChart(pieChart, (HashMap<String, Integer>) data.get("attended"));
            }
        });

        return rootView;
    }
    private void showPieChart(PieChart pieChart, HashMap<String, Integer> attendance){

        try{

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "";

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#304567"));
        colors.add(Color.parseColor("#309967"));
        colors.add(Color.parseColor("#476567"));
        colors.add(Color.parseColor("#890567"));
        colors.add(Color.parseColor("#a35567"));
        colors.add(Color.parseColor("#ff5f67"));
        colors.add(Color.parseColor("#3ca567"));

        //input data and fit data into pie chart entry
        for(String type: attendance.keySet()){
//            Log.d("harsh", type);
            pieEntries.add(new PieEntry(attendance.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries,label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
        }catch(Exception e){
            Log.d("harsh", e.getMessage());
        }
    }

}