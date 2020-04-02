package com.timelog.timelog.models;

public class JWTResponse {
    private String jwt;

    public JWTResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

}