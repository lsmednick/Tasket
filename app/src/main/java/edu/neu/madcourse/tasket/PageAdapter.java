package edu.neu.madcourse.tasket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PageAdapter extends FragmentPagerAdapter {
    private final int numTabs;
    private final String key;
    private final String teamType;
    private final FirebaseDatabase database;
    private static final String CURRENT_USER_ID = FirebaseAuth.getInstance().getUid();
    private ArrayList<String> userprivileges;
    private int lastPos;
    private String tempKey;

    public PageAdapter(@NonNull FragmentManager fm, int numOfTabs, String key, String teamType) {
        super(fm);
        this.numTabs = numOfTabs;
        this.key = key;
        this.teamType = teamType;
        this.database = FirebaseDatabase.getInstance();
        this.userprivileges = new ArrayList<>();
        userOwner();
        lastPos = 0;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment myFrag;
        Bundle myBundle = new Bundle();

        myBundle.putString("KEY", this.key);
        myBundle.putString("TYPE", this.teamType);

        // skip subteams if in subteam view
        if (teamType.equals("subteams")) {
            if (position > 0) {
                position++;
            }
        }
        if (!this.userprivileges.contains(this.key)) {
            if (position == 3) {
                position = lastPos;
            }
        }

        switch (position) {
            case 0:
                myFrag = new tabTask();
                break;

            case 1:
                myFrag = new tabSubteam();
                break;

            case 2:
                myFrag = new tabMember();
                break;

            case 3:
                myFrag = new tabPermission();
                break;

            default:
                myFrag = null;
        }
        if (myFrag != null) {
            this.lastPos = position;
            myFrag.setArguments(myBundle);
        }
        return myFrag;
    }

    @Override
    public int getCount() {
        return numTabs;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    private void userOwner() {
        ArrayList<String> priv = new ArrayList<String>();
        DatabaseReference userRef = this.database.getReference("Users/" + CURRENT_USER_ID + "/privileges");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnap : snapshot.getChildren()) {
                    priv.add(postSnap.getKey());
                }
                System.out.println(priv);
                setGlobalPrivileges(priv);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setGlobalPrivileges(ArrayList<String> privs) {
        this.userprivileges = privs;
        System.out.println("privs: " + this.userprivileges);

    }
  
}
