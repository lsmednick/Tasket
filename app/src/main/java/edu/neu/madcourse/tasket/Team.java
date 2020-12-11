package edu.neu.madcourse.tasket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Team {
    private Map<String, Boolean> associated_members;
    private ArrayList<Permission> permissions;
    private final HashMap<String, Boolean> subteams;
    private String teamName;
    //private ArrayList<Task> tasks;
    private String pushID;

    /**
     * Default constructor.  Used to create new team instance that will later be populated
     * and saved in the database.
     */
    public Team() {
        this.associated_members = new HashMap<String, Boolean>();
        this.permissions = new ArrayList<Permission>();
        this.subteams = new HashMap<String, Boolean>();
        this.teamName = "";
        //this.tasks = new ArrayList<Task>();
    }
  

    public Map<String, Boolean> getAssociated_members() {

        return associated_members;
    }


    public void setAssociated_members(ArrayList<String> members) {
        HashMap<String, Boolean> myMembers = new HashMap<String, Boolean>();
        for (String item : members) {
            myMembers.put(item, true);
        }
        this.associated_members = myMembers;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    public HashMap<String, Boolean> getSubteams() {
        return subteams;
    }

    public void setSubteams(ArrayList<String> subteams) {
        for (String item : subteams) {
            this.subteams.put(item, true);
        }
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPushID() {
        return pushID;
    }

    public void setPushID(String pushID) {
        this.pushID = pushID;
    }
}
