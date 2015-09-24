package com.skillkit.model;

import com.skillkit.utils.StringValidations;

import java.util.ArrayList;

/**
 * Created by Allan on 23/09/2015.
 */
public class ProjectData {

    private String projectName;
    private String description;
    private String projectManagerUserName;
    private String projectManagerName;
    private ArrayList<PersonData> team;

    public ProjectData(){
        this.team = new ArrayList<>();
    }

    public void setProjectManager(PersonData projectManager, String username){
        String PMName = projectManager.getFirstName() + projectManager.getLastName();
        String PMUsername = username;
        this.projectManagerName = PMName;
        this.projectManagerUserName = PMUsername;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void addMember(PersonData teamMate){
        this.team.add(teamMate);
    }

    public void removeTeam(PersonData teamMate){
        this.team.remove(teamMate);
    }

    public ArrayList<PersonData> getTeam(){
        return this.team;
    }
}
