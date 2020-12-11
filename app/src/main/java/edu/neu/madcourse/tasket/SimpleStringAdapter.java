package edu.neu.madcourse.tasket;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;

public class SimpleStringAdapter extends RecyclerView.Adapter<SimpleStringAdapter.SimpleViewHolder> {
    private final ArrayList<String> mydata;  // <teamName>
    private final Map<String, String> dataMap;  // <teamName, key>
    private final Activity thisActivity;
    private final Class newActivity;
    private final PageAdapter myPA;
    private final ViewPager myVP;
    private static final String CURRENT_USER_ID = FirebaseAuth.getInstance().getUid();

    public SimpleStringAdapter(Map<String, String> data, Activity activityName, Class newActivity) {
        dataMap = data;
        mydata = new ArrayList<>(data.keySet());
        thisActivity = activityName;
        this.newActivity = newActivity;
        this.myPA = null;
        this.myVP = null;


        if (mydata.size() == 0) {
            mydata.add("click to add!");
        }
    }

    public SimpleStringAdapter(ArrayList<String> data) {
        dataMap = null;
        mydata = data;
        thisActivity = null;
        this.newActivity = null;
        this.myPA = null;
        this.myVP = null;

        if (mydata.size() == 0) {
            mydata.add("click to add!");
        }
    }


    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.simple_text_row, parent, false);
        SimpleViewHolder myHolder = new SimpleViewHolder(listItem);
        myHolder.setIsRecyclable(false);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        String data = mydata.get(position);
        holder.textView.setText(data);
        if (!data.equals("click to add!")) {
            holder.textView.setOnClickListener(v -> {
                if (dataMap != null) {

                    // sends existing team's key, teamName, and type to ViewTeam
                    String key = dataMap.get(data);
                    if (key.equals(CURRENT_USER_ID)) {
                        return;
                    } else {

                        Intent to_view_team = new Intent(thisActivity, this.newActivity);
                        String teamType = "subteams";
                        if (this.thisActivity.getClass() == ViewTeams.class) {
                            teamType = "teams";
                        } else if (this.newActivity == OtherProfileFragment.class) {
                            teamType = "user";
                        }

                        to_view_team.putExtra("TEAM_KEY", key);
                        to_view_team.putExtra("TEAM_TYPE", teamType);
                        to_view_team.putExtra("USER_KEY", key);
                        thisActivity.startActivity(to_view_team);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mydata.get(0).equals("click to add!")) {
            return 0;
        }
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

