package edu.neu.madcourse.tasket;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Map;

public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.SimpleViewHolder> {
    private final ArrayList<String> mydata;
    private final Map<String, String> dataMap;
    private final Activity thisActivity;
    private final Class newActivity;

    public SimpleStringAdapter(Map<String, String> data, Activity activityName, Class newActivity) {
        dataMap = data;
        mydata = new ArrayList<>(data.keySet());
        thisActivity = activityName;
        this.newActivity = newActivity;
    }


    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.simple_text_row, parent, false);
        return new SimpleViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        String data = mydata.get(position);
        holder.textView.setText(data);
        holder.textView.setOnClickListener(v -> {
            String key = dataMap.get(data);
            //TODO start activity ViewTeam and pass along key
            Toast.makeText(v.getContext(), "CLICKED", Toast.LENGTH_LONG).show();
            Intent to_view_team = new Intent(thisActivity, this.newActivity);
            to_view_team.putExtra("MESSAGE_KEY", key);
            thisActivity.startActivity(to_view_team);
        });
    }

    @Override
    public int getItemCount() {
        return mydata.size();
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.simple_text_view_row);
        }

    }
}

