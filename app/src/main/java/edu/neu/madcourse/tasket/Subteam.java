package edu.neu.madcourse.tasket;

import java.util.ArrayList;

public class Subteam {

    private String subteamName;
    private ArrayList<String> managers;
    private ArrayList<String> members;
    private ArrayList<String> tasks;
    private String pushID;

    public Subteam(String name) {
        subteamName = name;
        managers = new ArrayList<>();
        members = new ArrayList<>();
        tasks = new ArrayList<>();
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

    public ArrayList<String> getManagers() {
        return this.managers;
    }

    public void setManagers(ArrayList<String> list) {
        this.managers = list;
    }

    public void addManager(String user) {
        this.managers.add(user);
    }

    public void removeManager(String toDelete) {
        this.managers.remove(toDelete);
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

    public String getSubteamName() {
        return this.subteamName;
    }


    public void setSubteamName(String newName) {
        this.subteamName = newName;
    }
}
