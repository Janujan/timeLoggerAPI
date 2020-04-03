package com.timelog.timelog.models;

public class JWTResponse {
    private String token;

    public JWTResponse(String token) {
        this.token = token;
    }

    public String getJwt() {
        return this.token;
    }

}