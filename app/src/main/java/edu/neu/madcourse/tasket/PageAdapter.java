package edu.neu.madcourse.tasket;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private final int numTabs;
    private final String key;
    private final String teamType;

    public PageAdapter(@NonNull FragmentManager fm, int numOfTabs, String key, String teamType) {
        super(fm);
        this.numTabs = numOfTabs;
        this.key = key;
        this.teamType = teamType;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment myFrag;
        Bundle myBundle = new Bundle();
        myBundle.putString("KEY", this.key);
        myBundle.putString("TYPE", this.teamType);

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
}
