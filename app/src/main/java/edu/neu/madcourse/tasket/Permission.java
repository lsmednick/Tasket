package edu.neu.madcourse.tasket;

import java.util.ArrayList;

public class Permission {
    private final String permissionName;
    private final ArrayList<Boolean> allowances;
    private final ArrayList<String> associated_members;


    /**
     * Default constructor.  Creates a blank permissions object that will be populated by the user
     */
    public Permission() {
        this.permissionName = "";
        this.allowances = new ArrayList<>();
        this.associated_members = new ArrayList<>();
    }

    /**
     * Constructs a permission object that represents a permission within a team or subteam
     *
     * @param name       String representing the name of the subteam
     * @param allowances ArrayList<Boolean> boolean value representing whether an action is permitted (true) or not (false) </Boolean>
     *                   in order according to the order of allowances presented when setting these values (standard order)
     * @param members    ArrayList<String> member username </String>
     */
    public Permission(String name, ArrayList<Boolean> allowances, ArrayList<String> members) {
        this.permissionName = name;
        this.associated_members = members;
        this.allowances = allowances;
    }
}
