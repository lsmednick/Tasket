package edu.neu.madcourse.tasket;

import android.util.Log;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

public class Team implements ParentObject {

    //private static final String TAG = Team.class.getSimpleName();
    private String teamName;
    private List<Object> subteamList;
    private ArrayList<String> owners;
    private ArrayList<String> members;
    private ArrayList<String> tasks;
    private String pushID;


    public Team() {

    }

    public Team(String name) {
        this.teamName = name;

        // initialize empty arrays
        this.members = new ArrayList<>();
        this.tasks = new ArrayList<>();
        this.owners = new ArrayList<>();

    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String name) {
        this.teamName = name;
    }

    public String getPushID() {
        return this.pushID;
    }

    public void setPushID(String id) {
        this.pushID = id;
    }


    /**
     * Get, set, add, remove owner
     * TODO add info to DB
     */

    public ArrayList<String> getOwners() {
        return this.owners;
    }

    public void setOwners(ArrayList<String> list) {
        this.owners = list;
    }

    public void addOwner(String user) {
        this.owners.add(user);
    }

    public void removeOwner(String toDelete) {
        this.owners.remove(toDelete);
    }

    /**
     * Get, set, add, remove member
     */

    public ArrayList<String> getMembers() {
        return this.members;
    }

    public void setMembers(ArrayList<String> list) {
        this.members = list;
    }

    public void addMember(String newMember) {
        this.members.add(newMember);
    }

    public void removeMember(String toDelete) {
        this.members.remove(toDelete);
    }

    /**
     * Get, set, add, remove tasks
     */

    public ArrayList<String> getTasks() {
        return this.tasks;
    }

    public void setTasks(ArrayList<String> list) {
        this.tasks = list;
    }

    public void addTask(String newTask) {
        this.tasks.add(newTask);
    }

    public void removeTask(String toDelete) {
        this.tasks.remove(toDelete);
    }

    public void removeSubteam(String name) {
        //TODO remove subteam object from list
    }

    @Override
    public List<Object> getChildObjectList() {
        return subteamList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        subteamList = list;
        //Log.i(TAG, "list added");
    }
}
