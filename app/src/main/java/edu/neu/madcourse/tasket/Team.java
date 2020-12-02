package edu.neu.madcourse.tasket;

import java.util.ArrayList;

public class Team {
    private ArrayList<String> associated_members;
    private ArrayList<Permission> permissions;
    private ArrayList<Subteam> subteams;
    private String teamName;
    //private ArrayList<Task> tasks;
    private String pushID;

    /**
     * Default constructor.  Used to create new team instance that will later be populated
     * and saved in the database.
     */
    public Team() {
        this.associated_members = new ArrayList<String>();
        this.permissions = new ArrayList<Permission>();
        this.subteams = new ArrayList<Subteam>();
        this.teamName = "";
        //this.tasks = new ArrayList<Task>();
    }

    /**
     * Existing team constructor.  Creates existing team object from information stored in DB
     *
     * @param members     ArrayList<String> member_username </String> representing all members of the team
     * @param permissions ArrayList<Permission> permission objects </Permission> representing all permission levels of the team.
     * @param subteams    ArrayList<Subteam> subteam objects </Subteam> representing all subteams associated with the team
     * @param name        String representing the name of the team
     *                    //@param tasks       ArrayList<Task> task object </Task> represents all tasks associated with the team at large (not subteams)
     *                    TODO to be added later
     */
    public Team(ArrayList<String> members, ArrayList<Permission> permissions, ArrayList<Subteam> subteams, String name) {
        this.associated_members = members;
        this.permissions = permissions;
        this.subteams = subteams;
        this.teamName = name;
        //this.tasks = tasks;
    }

    public ArrayList<String> getAssociated_members() {
        return associated_members;
    }

    public void setAssociated_members(ArrayList<String> associated_members) {
        this.associated_members = associated_members;
    }

    public ArrayList<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<Permission> permissions) {
        this.permissions = permissions;
    }

    public ArrayList<Subteam> getSubteams() {
        return subteams;
    }

    public void setSubteams(ArrayList<Subteam> subteams) {
        this.subteams = subteams;
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
