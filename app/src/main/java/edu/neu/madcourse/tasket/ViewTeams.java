package edu.neu.madcourse.tasket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewTeams extends AppCompatActivity {

    private RecyclerView myRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_teams);

        // create team objects associated with user so we can add them to the recycler view
        // ArrayList<ParentObject> myTeams = createTeams();

        // set up recycler view
        myRecycler = findViewById(R.id.teamNameRecyclerView);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        TeamExpandableAdaptor teamExpandableAdaptor = new TeamExpandableAdaptor(this, createTeams());
        teamExpandableAdaptor.setCustomParentAnimationViewId(R.id.parent_list_item_expand_arrow);
        teamExpandableAdaptor.setParentClickableViewAnimationDefaultDuration();
        teamExpandableAdaptor.setParentAndIconExpandOnClick(true);
        myRecycler.setAdapter(teamExpandableAdaptor);


        // go to create team activity
        FloatingActionButton createTeam = findViewById(R.id.addTeam);
        createTeam.setOnClickListener(v -> {
            Intent viewTeams_to_createTeam = new Intent(this, CreateTeam.class);
            startActivity(viewTeams_to_createTeam);
        });

    }

    private ArrayList<ParentObject> createTeams() {
        ArrayList<ParentObject> teams = new ArrayList<>();


        //temporary team building with default information
        for (int i = 0; i < 9; i++) {
            //generate sample subteams
            ArrayList<Object> subteams = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                subteams.add(new Subteam("subteam" + Integer.valueOf(j).toString()));
            }

            Team tempTeam = new Team("Team" + Integer.valueOf(i).toString());
            tempTeam.setChildObjectList(subteams);
            teams.add(tempTeam);
        }
        return teams;
    }

}