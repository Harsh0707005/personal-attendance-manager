package com.thattechyguy.personalattendancemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleManageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private schedulesCardAdapter adapter;
    private ArrayList<HashMap<String, String>> dataList;

    public ScheduleManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleManageFragment newInstance(String param1, String param2) {
        ScheduleManageFragment fragment = new ScheduleManageFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_schedule_manage, container, false);

        RecyclerView schedulesRecyclerView = rootView.findViewById(R.id.schedulesRecyclerView);

//        dataList = new ArrayList<HashMap<String, String>>();

        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        if (adapter == null) {
            adapter = new schedulesCardAdapter(rootView.getContext(), dataList);
            schedulesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            schedulesRecyclerView.setAdapter(adapter);
        }

        loadSchedules();

        return rootView;
    }
    private void loadSchedules(){
        firebaseManage firebase = new firebaseManage();
        firebase.getScheduleData("iUXcZmmL2nZvZvFBIwMXG3hm2VP2", new ArraylistHashMapCallback() {
            @Override
            public void onCallback(ArrayList<HashMap<String, String>> data) {
                dataList.clear(); // Clear existing data
                dataList.addAll(data); // Add new data
                adapter.notifyDataSetChanged();
            }
        });
    }
}