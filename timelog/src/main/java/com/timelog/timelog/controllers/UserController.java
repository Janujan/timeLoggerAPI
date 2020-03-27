package com.timelog.timelog.controllers;

import java.util.HashMap;

import com.timelog.timelog.models.User;
import com.timelog.timelog.models.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController{

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value="/register/user", method=RequestMethod.POST)
    public HashMap<String, String> registerUser(@RequestBody User user){   
        //check if username/email already exist
        //I can refactor this code to search for : emailorUsername
        HashMap<String, String> response = new HashMap<String, String>();
        
        User result = userRepository.findByUsername(user.getUsername());
        boolean uniqueUserName = true;
        boolean uniqueEmail = true;

        if(result != null){
            uniqueUserName = false;
            response.put("error", "username exists already");
        }
        
        result = userRepository.findByEmail(user.getEmail());

        if(result != null){
            uniqueEmail = false;
            response.clear();
            response.put("error", "this email is already associated with another username");
        }

        if(uniqueEmail == true && uniqueUserName == true){
            userRepository.save(user);
            response.put("id", Long.toString(user.getId()));
            return response;
        }
        else{
            return response;
        }

    }
}