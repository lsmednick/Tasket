package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewTeams extends AppCompatActivity {

    private static final String CURRENT_USER_KEY = FirebaseAuth.getInstance().getUid();
    private RecyclerView team_name_recycler;
    private FloatingActionButton add_team_fab;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private final Activity thisActivity = this;
    private HashMap<String, String> myMap;
    private SimpleStringAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teams);

        this.mAuth = FirebaseAuth.getInstance();
        this.database = FirebaseDatabase.getInstance();

        this.myMap = new HashMap<>();
        this.team_name_recycler = findViewById(R.id.view_teams_recycler);
        this.myAdapter = new SimpleStringAdapter(this.myMap, this, ViewTeam.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        team_name_recycler.setLayoutManager(layoutManager);
        team_name_recycler.setAdapter(myAdapter);

        //tempsetup();

        //initialize on screen views
        add_team_fab = findViewById(R.id.view_teams_fab);

        //set on click for new team
        add_team_fab.setOnClickListener(v -> {
            //pop up dialog with text entry
            Activity myActivity = this.thisActivity;
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Create Team");
            alert.setMessage("Enter the team name");

            // Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String newTeam = input.getText().toString();
                    Intent intent = new Intent(myActivity, ViewTeam.class);
                    String teamKey = createTeam(newTeam);
                    intent.putExtra("TEAM_KEY", teamKey);
                    intent.putExtra("TEAM_TYPE", "teams");
                    startActivity(intent);

                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        });


        setUpData();
    }

    private String createTeam(String teamName) {
        DatabaseReference teamRef = this.database.getReference("teams");
        DatabaseReference pushID = teamRef.push();
        pushID.child("teamName").setValue(teamName);

        DatabaseReference newRef = this.database.getReference("Users/" + CURRENT_USER_KEY + "/" + "teams");
        newRef.child(pushID.getKey()).setValue(true);

        return pushID.getKey();
    }

    private void setUpData() {
        final String userKey = mAuth.getUid();
        ArrayList<String> keys = new ArrayList<>();
        DatabaseReference ref = this.database.getReference("Users/" + userKey + "/teams");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                keys.clear();
                for (DataSnapshot postSnap : snapshot.getChildren()) {
                    keys.add(postSnap.getKey());
                }
                makeMap(keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void makeMap(ArrayList<String> keys) {
        //this.myMap = new HashMap<String, String>();
        for (String key : keys) {
            DatabaseReference ref = this.database.getReference("teams/" + key);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        System.out.println("VIEW TEAMS   " + snapshot.toString());
                        String name = snapshot.child("teamName").getValue().toString();
                        myMap.put(name, key);
                        System.out.println("VIEW TEAMS MAP: " + myMap.toString());

                    }
                    System.out.println("MAP: " + myMap.size());
                    team_name_recycler.setAdapter(new SimpleStringAdapter(myMap, thisActivity, ViewTeam.class));
                    System.out.println("ADAPTER: " + myAdapter.getItemCount());
                    System.out.println("MAP: " + myMap.size());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }


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
        DatabaseReference userTeams = database.getReference("Users/" + mAuth.getUid() + "/teams");
        userTeams.push().setValue(pushID);

    }
}