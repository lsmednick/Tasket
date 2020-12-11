package edu.neu.madcourse.tasket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
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
    private String teamType;

    private Team currentTeam;
    final static String CURRENT_USER_KEY = FirebaseAuth.getInstance().getUid();
    private ArrayList<String> userprivileges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_team);

        this.database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        String teamKey = intent.getStringExtra("TEAM_KEY");
        this.teamType = intent.getStringExtra("TEAM_TYPE");

        this.userprivileges = new ArrayList<>();
        userOwner(teamKey);

        System.out.println("ViewTeamKeys: " + teamKey + " " + teamType);


        teamName = findViewById(R.id.view_team_text_view);


        // existing
        if (teamKey != null) {
            getTeamInfo(teamKey, this.database);

        }


        this.tabLayout = findViewById(R.id.ViewTeamTabLayout);
        this.taskTab = findViewById(R.id.ViewTeamTaskTab);
        this.subteamTab = findViewById(R.id.ViewTeamSubteamTab);
        this.memberTab = findViewById(R.id.ViewTeamMemberTab);
        this.permissionTab = findViewById(R.id.ViewTeamPermissionTab);
        this.viewPager = findViewById(R.id.ViewTeamViewPager);

        // change UI for subteams - all other functionality is the same
        if (teamType.equals("subteams")) {
            this.tabLayout.setBackgroundColor(getResources().getColor(R.color.green_2));
            this.teamName.setBackgroundColor(getResources().getColor(R.color.green_2));
            //TODO figure out how to change the color of the individual tabs
            tabLayout.removeTabAt(1);
        }



        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), teamKey, teamType);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    public PageAdapter getPageAdapter() {
        return this.pageAdapter;
    }

    public void getTeamInfo(String key, FirebaseDatabase database) {


        DatabaseReference teamRef = database.getReference(this.teamType + "/" + key);

        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String teamName = "";

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    if (postSnapshot.getKey().equals("teamName")) {
                        teamName = postSnapshot.getValue().toString();
                    }

                    //TODO implement permissions and subteams
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

    private void userOwner(String teamkey) {

        ArrayList<String> priv = new ArrayList<String>();
        DatabaseReference userRef = this.database.getReference("Users/" + CURRENT_USER_KEY + "/privileges");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnap : snapshot.getChildren()) {
                    priv.add(postSnap.getKey());
                }
                System.out.println(priv);
                setGlobalPrivileges(priv, teamkey);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setGlobalPrivileges(ArrayList<String> privs, String key) {
        this.userprivileges = privs;
        if (!userprivileges.contains(key)) {
            tabLayout.removeTabAt(3);
            this.tabLayout.setBackgroundColor(getResources().getColor(R.color.blue_2));

        }
        System.out.println("privs: " + this.userprivileges);

    }

}