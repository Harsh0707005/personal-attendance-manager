package com.thattechyguy.personalattendancemanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.thattechyguy.personalattendancemanager.Interfaces.booleanSuccessCallback;

import java.util.ArrayList;
import java.util.HashMap;


public class schedulesCardAdapter extends RecyclerView.Adapter<schedulesCardAdapter.ViewHolder> {

    private ArrayList<HashMap<String, Object>> dataList;
    private Context context;
    private firebaseManage firebase;
    private String uid;

    public schedulesCardAdapter(Context context, ArrayList<HashMap<String, Object>> dataList){
        this.context = context;
        this.dataList = dataList;

        firebase = new firebaseManage();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        TextView deleteScheduleBtn = holder.deleteScheduleBtn;
        CardView cardView = holder.cardView;


        String uniqueId = (String) dataList.get(position).get("uniqueId");
        String name = (String) dataList.get(position).get("scheduleName");
        String attended = (String) dataList.get(position).get("numAttended");
        String total = (String) dataList.get(position).get("numTotal");
        String desc = (String) dataList.get(position).get("scheduleDescription");
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
                Intent i = new Intent(context, classes.class);
                i.putExtra("path", "/attendance/"+uid+"/"+ uniqueId +"/");
                context.startActivity(i);
            }
        });

        deleteScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Caution");
                alert.setMessage("Are you sure you want to delete the schedule?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebase.deleteSchedule(String.format("/attendance/%s/%s/", uid, uniqueId), new booleanSuccessCallback() {
                            @Override
                            public void onCallback(boolean success) {
                                if (success) {
                                    Toast.makeText(context, "Schedule Deleted Successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(context, "Error deleting Schedule", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

//                firebase.deleteSchedule(String.format("/attendance/%s/%s/", uid, uniqueId), new booleanSuccessCallback() {
//                    @Override
//                    public void onCallback(boolean success) {
//                        if (success) {
//                            Toast.makeText(context, "Schedule Deleted Successfully", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(context, "Error deleting Schedule", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleName, scheduleDescription, numAttended, numTotal, numPercent, deleteScheduleBtn;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            scheduleName = itemView.findViewById(R.id.scheduleName);
            scheduleDescription = itemView.findViewById(R.id.scheduleDescription);
            numAttended = itemView.findViewById(R.id.numAttended);
            numTotal = itemView.findViewById(R.id.numTotal);
            numPercent = itemView.findViewById(R.id.numPercent);
            cardView = itemView.findViewById(R.id.cardView);
            deleteScheduleBtn = itemView.findViewById(R.id.deleteScheduleBtn);
        }
    }
}
