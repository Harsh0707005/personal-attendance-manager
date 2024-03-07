package com.thattechyguy.personalattendancemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thattechyguy.personalattendancemanager.Interfaces.ArraylistHashMapCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.HashMapObjectCallback;
import com.thattechyguy.personalattendancemanager.Interfaces.intSuccessCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        firebaseManage firebase = new firebaseManage();
        firebase.getAttendanceDate("attendance/iUXcZmmL2nZvZvFBIwMXG3hm2VP2/-NsEeLF-F9LI606yTyf7", new HashMapObjectCallback() {
            @Override
            public void onCallback(HashMap<String, Object> data) {
                Log.d("harsh", String.valueOf(data));
            }
        });
//        try{
//            firebase.getLastAddedDate("attendance/iUXcZmmL2nZvZvFBIwMXG3hm2VP2/-NsEeLF-F9LI606yTyf7", new intSuccessCallback() {
//                @Override
//                public void onCallback(int success) {
//                    Log.d("harsh", String.valueOf(success));
//                }
//            });
//        }catch(Exception e){
//            Log.d("harsh", e.getMessage());
//        }
        return rootView;
    }
}