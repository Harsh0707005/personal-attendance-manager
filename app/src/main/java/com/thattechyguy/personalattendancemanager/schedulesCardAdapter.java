package com.thattechyguy.personalattendancemanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;


public class schedulesCardAdapter extends RecyclerView.Adapter<schedulesCardAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> dataList;
    private Context context;

    public schedulesCardAdapter(Context context, ArrayList<HashMap<String, String>> dataList){
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public schedulesCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull schedulesCardAdapter.ViewHolder holder, int position) {

        TextView scheduleName = holder.scheduleName;
        TextView scheduleDescription = holder.scheduleDescription;
        TextView numAttended = holder.numAttended;
        TextView numTotal = holder.numTotal;
        TextView numPercent = holder.numPercent;

        String name = dataList.get(position).get("scheduleName");
        String attended = dataList.get(position).get("numAttended");
        String total = dataList.get(position).get("numTotal");
        String desc = dataList.get(position).get("scheduleDescription");
        String percent = String.valueOf((Integer.parseInt(attended)*100)/Integer.parseInt(total));

        scheduleName.setText(name);
        if (desc!="null") {
            scheduleDescription.setText(desc);
        }
        numAttended.setText("Attended: " + attended);
        numTotal.setText("Total: " + total);
        numPercent.setText(percent+"%");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleName, scheduleDescription, numAttended, numTotal, numPercent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scheduleName = itemView.findViewById(R.id.scheduleName);
            scheduleDescription = itemView.findViewById(R.id.scheduleDescription);
            numAttended = itemView.findViewById(R.id.numAttended);
            numTotal = itemView.findViewById(R.id.numTotal);
            numPercent = itemView.findViewById(R.id.numPercent);
        }
    }
}
