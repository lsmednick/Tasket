package edu.neu.madcourse.tasket;

import java.util.ArrayList;

public class Subteam {
    private final ArrayList<String> associated_members;
    private final ArrayList<Permission> permissions;
    //private ArrayList<Task> tasks;
    private final String subteamName;
    private String pushID;

    /**
     * Default constructor.  Used to create empty subteam that will be populated by the user
     */
    public Subteam() {
        this.associated_members = new ArrayList<>();
        this.permissions = new ArrayList<>();
        //this.tasks = new ArrayList<Task>();
        this.subteamName = "";

    }

    /**
     * Existing subteam constructor.  Creates existing subteam object from information stored in DB
     *
     * @param members     ArrayList<String> member_username </String> representing all members of the subteam
     * @param permissions ArrayList<Permission> permission objects </Permission> representing all permission levels of the subteam.
     * @param name        String representing the name of the subteam
     *                    //@param tasks       ArrayList<Task> task object </Task> represents all tasks associated with the subteam specifically
     *                    TODO add when joined with task branch
     */
    public Subteam(ArrayList<String> members, ArrayList<Permission> permissions, String name) {
        this.associated_members = members;
        this.permissions = permissions;
        this.subteamName = name;
        //this.tasks = tasks;
    }

}
