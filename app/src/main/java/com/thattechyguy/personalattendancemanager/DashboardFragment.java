package com.thattechyguy.personalattendancemanager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.HashMapObjectCallback;

import java.util.ArrayList;
import java.util.HashMap;

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

    private Spinner scheduleSpinner;
    private String uid;
    private FirebaseAuth mAuth;
    private firebaseManage firebase;
    private PieChart pieChart;
    private int totalHeight;

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
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        scheduleSpinner = rootView.findViewById(R.id.scheduleSpinner);

        ExpandableListView expandableListOverview = rootView.findViewById(R.id.expandableListOverview);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        firebase = new firebaseManage();

        pieChart = rootView.findViewById(R.id.pieChart_view);

        loadSpinner(uid, scheduleSpinner, expandableListOverview);
        return rootView;
    }
    private void loadSpinner(String uid, Spinner spinner, ExpandableListView expandableListOverview){

//        [{scheduleName=College, numTotal=171, numAttended=91, scheduleDescription=Schedule for college, uniqueId=-NndQUc2kgcjJSH2-lw9}, {scheduleName=abc, numTotal=5, numAttended=2, scheduleDescription=, uniqueId=-NsEeLF-F9LI606yTyf7}, {scheduleName=hahah, numTotal=8, numAttended=5, scheduleDescription=, uniqueId=-Ns-T8wOOHI8ED4km4Rx}]
        firebase.getScheduleData(uid, new ArraylistHashMapCallback() {
            @Override
            public void onCallback(ArrayList<HashMap<String, Object>> data) {
                ArrayList<String> scheduleNames = new ArrayList<>();

                for (HashMap<String, Object> schedule:data){
//                    Log.d("harsh", String.valueOf(schedule.get("scheduleName")));
                    scheduleNames.add(schedule.get("scheduleName").toString());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, scheduleNames);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        showData(uid, data.get(position).get("uniqueId").toString(), expandableListOverview);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });
    }
    private void showData(String uid, String uniqueId, ExpandableListView expandableListOverview){

//        {attended={BEE=14, EFE=8, EM=10, Maths-2=14, Mech. Workshop=4, BEE Lab=5, BME=11, BME Lab=5, Physics Lab=5, EL & EC Workshop=3, Physics=9, EM Lab=3}, numTotal=171, total={BEE=24, EFE=15, EM=16, Maths-2=32, Mech. Workshop=8, BEE Lab=7, BME Lab=8, BME=17, Physics Lab=9, EL & EC Workshop=9, EM Lab=8, Physics=18}, numAttended=91, classes=[EM Lab, BEE, Maths-2, Mech. Workshop, BME Lab, EFE, BME, Physics, EL & EC Workshop, EM, Physics Lab, BEE Lab]}

        firebase.getAttendanceDate(String.format("attendance/%s/%s", uid, uniqueId), new HashMapObjectCallback() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
//                Log.d("harsh", String.valueOf(data));
                showPieChart(pieChart, (HashMap<String, Integer>) data.get("attended"));
                try{
                    expandableOverviewAdapter adapter = new expandableOverviewAdapter(getContext(), data);
                    expandableListOverview.setAdapter(adapter);


                    expandableListOverview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            int temp_height = totalHeight;
                            ExpandableListAdapter listAdapter = expandableListOverview.getExpandableListAdapter();
                            View childView = listAdapter.getChildView(groupPosition, 0, false, null, expandableListOverview);
                            childView.measure(
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            totalHeight += childView.getMeasuredHeight(); // increasing height

                            ViewGroup.LayoutParams params = expandableListOverview.getLayoutParams();
                            params.height = totalHeight;
                            expandableListOverview.setLayoutParams(params);
                        }
                    });
                    expandableListOverview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                        @Override
                        public void onGroupCollapse(int groupPosition) {
                            ExpandableListAdapter listAdapter = expandableListOverview.getExpandableListAdapter();
                            View childView = listAdapter.getChildView(groupPosition, 0, false, null, expandableListOverview);
                            childView.measure(
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                            totalHeight -= childView.getMeasuredHeight(); // reducing the height

                            ViewGroup.LayoutParams params = expandableListOverview.getLayoutParams();
                            params.height = totalHeight;
                            expandableListOverview.setLayoutParams(params);
                        }
                    });

                    adjustHeight(expandableListOverview);
                }catch(Exception e){
                    Log.d("harsh", e.getMessage());
                }
            }
        });
    }
    private void adjustHeight(ExpandableListView expandableListOverview){
        ExpandableListAdapter listAdapter = expandableListOverview.getExpandableListAdapter();
        if (listAdapter == null) {
            return;
        }
        totalHeight = 0;
        int numberOfGroups = listAdapter.getGroupCount();
        for (int i = 0; i < numberOfGroups; i++) {
            View groupView = listAdapter.getGroupView(i, false, null, expandableListOverview);
            groupView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            totalHeight += groupView.getMeasuredHeight();

            if (expandableListOverview.isGroupExpanded(i)) {
                View childView = listAdapter.getChildView(i, 0, false, null, expandableListOverview);
                childView.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalHeight += childView.getMeasuredHeight();
            }
        }

        totalHeight += expandableListOverview.getDividerHeight() * (numberOfGroups - 1);

        ViewGroup.LayoutParams params = expandableListOverview.getLayoutParams();
        params.height = totalHeight;
        expandableListOverview.setLayoutParams(params);
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