package com.skillkit.model;

/**
 * Created by Allan on 13/09/2015.
 */
public class skillData {
    private String skillName;
    private String skillRate;
    private String error;

    public void setSkillName(String name){
        this.skillName = name;
    }
    public String getSkillName(){
        return this.skillName;
    }

    public void setSkillRate(String rate){
        this.skillRate = rate;
    }

    public String getSkillRate(){
        return this.skillRate;
    }
    public String getError(){
        return this.error;
    }

    public void setError(String error){
        this.error = error;
    }
}
