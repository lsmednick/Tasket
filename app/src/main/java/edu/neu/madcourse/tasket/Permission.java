package edu.neu.madcourse.tasket;

import java.util.ArrayList;
import java.util.Arrays;

public class Permission {
    private ArrayList<Boolean> permissionSettings;
    private String permissionName;
    private String[] associated_members;

    public Permission() {

    }

    public Permission(ArrayList<Boolean> settings, String name, String[] members) {

        this.permissionName = name;
        this.permissionSettings = settings;
        this.associated_members = members;
    }

    public ArrayList<Boolean> getPermissionSettings() {
        return permissionSettings;
    }

    public void setPermissionSettings(ArrayList<Boolean> permissionSettings) {
        this.permissionSettings = permissionSettings;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String[] getAssociated_members() {
        return associated_members;
    }

    public void setAssociated_members(String[] associated_members) {
        this.associated_members = associated_members;
    }

    public void add_member(String username) {
        this.associated_members = Arrays.copyOf(this.associated_members, this.associated_members.length + 1);
        this.associated_members[this.associated_members.length - 1] = username;
    }

    public void remove_member(String username) {
        for (int i = 0; i < this.associated_members.length; i++) {
            if (this.associated_members[i].equals(username)) {
                if (i == 0) {
                    this.associated_members = Arrays.copyOfRange(this.associated_members, 1, this.associated_members.length);
                } else {
                    String[] newString = new String[this.associated_members.length - 1];
                    for (int j = 0; j < this.associated_members.length - 2; j++) {
                        if (j != i) {
                            newString[j] = this.associated_members[j];
                        }
                    }
                    this.associated_members = newString;
                }
            }
        }
    }
}
