package com.timelog.timelog.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Log{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private double duration;

    @NotNull
    @Column
    private String task;

    @Column
    private String tag;

    
    @NotNull
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(referencedColumnName="id", name="user")
    private User user;

    public void setid(long id){
        this.id = id;
    }

    public long getid(){
        return this.id;
    }
    public double getDuration(){
        return this.duration;
    }

    public void setDuration(double duration){
        this.duration = duration;
    }

    public String getTask(){
        return this.task;
    }

    public void setTask(String task){
        this.task = task;
    }

    public String getTag(){
        return this.tag;
    }

    public void setTag(String tag){
        this.tag = tag;
    }
    public String getDescription(){
        return this.description;
    }

    public void setDescription(String desc){
        this.description = desc;
    }

    public User getUser(){
        return this.user;
    }

    public void setUser(User user){
        this.user = user;
    }
    
    @Override
    public String toString(){
        String str = "Log Data: \n";
        str+= " Task: " + this.task + "\n";
        str+= " Description: " + this.description + "\n";
        str+= " Tag: " + this.tag + "\n";
        str+= " User: " + this.user.getUsername() + "\n";
        return str;
    }

    //equality operator between two logs
    public boolean equals(Log compare){
        boolean result = true;
        if(this.duration != compare.duration){
            result = false;
        }
        if(!this.task.equals(compare.task)){
            result = false;
        }
        if(!this.description.equals(compare.description)){
            result = false;
        }

        //tag can be null
        if(this.tag == null){
            if(compare.tag != null){
                result = false;
            }
        }
        else{
            if(!this.tag.equals(compare.tag)){
                result = false;
            }
        }
        return result;
    }

}