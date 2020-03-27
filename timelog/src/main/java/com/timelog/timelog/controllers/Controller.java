package com.timelog.timelog.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller{
    
    //This is the basic endpoint when people hit the API
    @ResponseStatus(value = HttpStatus.OK, reason = "")
    @RequestMapping(value="/", method=RequestMethod.GET)
    public HashMap<String,String> frontDoor() {
        HashMap<String,String> response = new HashMap<String,String>();
        response.put("Hello!","Welcome to the Time Logger Application");
        return response;
    }

    //Error response for all invalid routes
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "")
    @RequestMapping(value="**", method=RequestMethod.GET)
    public HashMap<String,String> errorMethod() {
        HashMap<String,String> response = new HashMap<String,String>();
        response.put("Error!","This endpoint doesnt exist!");
        return response;
    }
}