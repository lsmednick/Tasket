package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewTeams extends AppCompatActivity {

    private RecyclerView team_name_recycler;
    private FloatingActionButton add_team_fab;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teams);

        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();


        setUpData();
        //tempsetup();

        //initialize on screen views
        add_team_fab = findViewById(R.id.view_teams_fab);

        //set on click for new team
        add_team_fab.setOnClickListener(v -> {
            //TODO go to create team
        });


    }

    private void setUpData() {
        final String userKey = mAuth.getUid();
        System.out.println("USER KEY:" + userKey);

        //get list of all teams for user
        DatabaseReference userTeams = database.getReference("users/" + userKey);
        HashMap<String, String> teamMap = new HashMap<>();
        ArrayList<String> teamKeys = new ArrayList<>();
        userTeams.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teamKeys.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println("KEY : " + postSnapshot.getKey());
                    System.out.println(postSnapshot.getKey().equals("teams"));
                    if (postSnapshot.getKey().equals("teams")) {
                        System.out.println("CHILDREN = " + snapshot.getChildrenCount());
                        if (postSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot childSnap : postSnapshot.getChildren()) {
                                teamKeys.add(childSnap.getValue().toString());
                                System.out.println("KEY VALUE = " + childSnap.getValue().toString());
                            }
                        }


                        //teamKeys.add(postSnapshot.getValue().toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("VIEWTEAMS", "user team data access failed");
            }
        });
        System.out.println(teamKeys.toString());
        DatabaseReference teamData = database.getReference("teams");
        teamData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println(postSnapshot.getKey());
                    if (teamKeys.contains(postSnapshot.getKey())) {
                        System.out.println("FOUND");
                        String value = postSnapshot.getKey();
                        String key = postSnapshot.child("teamName").getValue().toString();
                        teamMap.put(key, value);
                    }
                }
                System.out.println(teamMap.toString() + "\nSIZE: " + teamMap.size());

                setRecycler(teamMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setRecycler(Map<String, String> map) {

        ArrayList<String> teamNames = new ArrayList<>(map.keySet());
        System.out.println("ARRAY SIZE = " + teamNames.size());
        team_name_recycler = findViewById(R.id.view_teams_recycler);
        SimpleStringAdapter adapter = new SimpleStringAdapter(map, this, ViewTeam.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        team_name_recycler.setLayoutManager(layoutManager);
        team_name_recycler.setAdapter(adapter);
        System.out.println("ADAPTER SIZE = " + adapter.getItemCount());
    }


    public void tempsetup() {
        //create temporary team to save to DB and pass around
        //no subteams or permissions for now
        ArrayList<String> members = new ArrayList<>();
        members.add("user1");
        members.add("user2");

        //create team
        Team tempTeam = new Team();
        tempTeam.setAssociated_members(members);
        tempTeam.setTeamName("tempteam name2");

        //access db
        DatabaseReference myRef = database.getReference("teams");
        DatabaseReference key = myRef.push();
        String pushID = key.getKey();
        tempTeam.setPushID(pushID);
        key.setValue(tempTeam);

        //add team to user's teams
        DatabaseReference userTeams = database.getReference("users/" + mAuth.getUid() + "/teams");
        userTeams.push().setValue(pushID);

    }
}