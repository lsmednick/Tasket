package edu.neu.madcourse.tasket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HourlyLogAdapter extends RecyclerView.Adapter<HourlyLogAdapter.MyViewHolder> {
    ArrayList<String> datesList;
    ArrayList<String> hoursList;
    private Context myContext;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, hours;

        public MyViewHolder(View item) {
            super(item);
            date = item.findViewById(R.id.hourlyCardDate);
            hours = item.findViewById(R.id.hourlyCardHours);
        }
    }

    public HourlyLogAdapter(Context mContext, ArrayList<String> date, ArrayList<String> hour) {
        datesList = date;
        hoursList = hour;
        myContext = mContext;
    }

    @NonNull
    @Override
    public HourlyLogAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_log_card, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.date.setText(datesList.get(position));
        holder.hours.setText(hoursList.get(position));
    }

    @Override
    public int getItemCount() {
        return datesList.size();
    }
}