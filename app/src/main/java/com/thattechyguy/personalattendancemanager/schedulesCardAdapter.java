package com.thattechyguy.personalattendancemanager;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        CardView cardView = holder.cardView;


        String uniqueId = dataList.get(position).get("uniqueId");
        String name = dataList.get(position).get("scheduleName");
        String attended = dataList.get(position).get("numAttended");
        String total = dataList.get(position).get("numTotal");
        String desc = dataList.get(position).get("scheduleDescription");
        if (!total.equals("0") && !attended.equals("0")) {
            String percent = String.valueOf((Integer.parseInt(attended) * 100) / Integer.parseInt(total));
            numPercent.setText(percent+"%");
        }else {
            numPercent.setText("0%");
        }

        scheduleName.setText(name);
        if (desc!="null") {
            scheduleDescription.setText(desc);
        }
        numAttended.setText("Attended: " + attended);
        numTotal.setText("Total: " + total);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent i = new Intent(context, classes.class);
                i.putExtra("path", "/attendance/"+uid+"/"+ uniqueId +"/");
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleName, scheduleDescription, numAttended, numTotal, numPercent;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scheduleName = itemView.findViewById(R.id.scheduleName);
            scheduleDescription = itemView.findViewById(R.id.scheduleDescription);
            numAttended = itemView.findViewById(R.id.numAttended);
            numTotal = itemView.findViewById(R.id.numTotal);
            numPercent = itemView.findViewById(R.id.numPercent);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
