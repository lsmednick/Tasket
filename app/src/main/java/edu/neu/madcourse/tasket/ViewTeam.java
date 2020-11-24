package edu.neu.madcourse.tasket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewTeam extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem taskTab;
    private TabItem subteamTab;
    private TabItem memberTab;
    private TabItem permissionTab;
    private PageAdapter pageAdapter;
    private TextView teamName;
    private FirebaseDatabase database;

    private Team currentTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);

        Intent intent = getIntent();
        String teamKey = intent.getStringExtra("MESSAGE_KEY");

        this.database = FirebaseDatabase.getInstance();
        getTeamInfo(teamKey, this.database);

        teamName = findViewById(R.id.view_team_text_view);
        tabLayout = findViewById(R.id.ViewTeamTabLayout);
        taskTab = findViewById(R.id.ViewTeamTaskTab);
        subteamTab = findViewById(R.id.ViewTeamSubteamTab);
        memberTab = findViewById(R.id.ViewTeamMemberTab);
        permissionTab = findViewById(R.id.ViewTeamPermissionTab);
        viewPager = findViewById(R.id.ViewTeamViewPager);


        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), teamKey, "team");
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 2) {
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 3) {
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public void getTeamInfo(String key, FirebaseDatabase database) {
        this.currentTeam = new Team();
        DatabaseReference teamRef = database.getReference("teams").child(key);
        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> members = new ArrayList<>();
                String teamName = "";

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    System.out.println("KEY: " + postSnapshot.getKey() + " VALUE: " + postSnapshot.getValue());
                    if (postSnapshot.getKey().equals("associated_members")) {
                        for (DataSnapshot member : postSnapshot.getChildren()) {
                            members.add(member.getValue().toString());
                        }
                    } else if (postSnapshot.getKey().equals("teamName")) {
                        teamName = postSnapshot.getValue().toString();
                    }

                    //TODO implement permissions and subteams
                }
                if (members.size() != 0) {
                    addMembers(members);
                }
                if (teamName != "") {
                    setName(teamName);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void setName(String name) {
        name = "  " + name;
        this.teamName.setText(name);
    }

    public void addMembers(ArrayList<String> members) {
        this.currentTeam.setAssociated_members(members);
        System.out.println("TEAM MEMBERS: " + this.currentTeam.getAssociated_members().toString());
    }
}