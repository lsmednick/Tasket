package edu.neu.madcourse.tasket;

public class Subteam {

    private String subteamName;

    public Subteam(String name) {
        subteamName = name;
    }

    // getters
    public String getSubteamName() {
        return this.subteamName;
    }

    // setters
    public void setSubteamName(String newName) {
        this.subteamName = newName;
    }
}
