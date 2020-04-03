package com.timelog.timelog.models;

// class corresponds to usercredentials json object for authentication
public class UserCredentials {

    private String username;
    private String password;

    public UserCredentials(){}

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}