package edu.neu.madcourse.tasket;

import android.util.Log;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

public class Team implements ParentObject {

    private static final String TAG = Team.class.getSimpleName();
    private String teamName;
    private List<Object> subteamList;

    public Team(String name) {
        this.teamName = name;

    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String name) {
        this.teamName = name;
    }

    @Override
    public List<Object> getChildObjectList() {
        return subteamList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        subteamList = list;
        Log.i(TAG, "list added");
    }
}
