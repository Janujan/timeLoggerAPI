package com.timelog.timelog.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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


    @Column
    private LocalDateTime timestamp;


    @JsonIgnore
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
    
    // example of timestamp passed in 2017-01-01 00:08:57.231
    public void setTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");
        try{
            this.timestamp = LocalDateTime.parse(timestamp, formatter);
        }
        catch(DateTimeParseException e){
            this.timestamp = null;
            System.out.println("invalid timestamp provided: " + e.getMessage());
        }
    }

    public LocalDateTime getTimestamp(){
        return this.timestamp;
    }

    @Override
    public String toString(){
        String str = "Log Data: \n";
        str+= " Task: " + this.task + "\n";
        str+= " Description: " + this.description + "\n";
        str+= " Tag: " + this.tag + "\n";
        str+= " User: " + this.user.getUsername() + "\n";
        str+= " Timestamp: " + this.getTimestamp().toString();
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

        //tag can be null, so if they are both null, dont compare
        if(this.tag != null && compare.tag != null){
            if(!this.tag.equals(compare.tag)){
                result = false;
            }
        }
        else if(this.tag == null && compare.tag != null){
            result = false;
        }
        else if(this.tag != null && compare.tag ==null){
            result = false;
        }

        if(this.timestamp != null && compare.timestamp != null){
            // System.out.println("old time: " +  this.timestamp.toString());
            // System.out.println("new time: " + compare.timestamp.toString());
            if(!this.timestamp.equals(compare.timestamp)){
                result = false;
                System.out.println("timestamps not equal");
            }
        }
        else{
            result = false;
        }
        return result;
    }

}