package com.timelog.timelog.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import com.timelog.timelog.models.Log;
import com.timelog.timelog.models.LogRepository;
import com.timelog.timelog.models.User;
import com.timelog.timelog.models.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private UserRepository userRepository;

    // @ResponseStatus(value = HttpStatus.OK, reason = "")
    //This function will now get user based on user id passed in via path variable
    //Then it will add the log under that user.
    @RequestMapping(value="/logs/submit/{username}", method=RequestMethod.POST)
    public HashMap<String,String> submitLog(@PathVariable String username, @RequestBody Log submitData){

        HashMap<String, String> response = new HashMap<String,String>();

        User qUser = userRepository.findByUsername(username);
    

        if(qUser == null){
            response.put("error", "user doesnt exist, register first");
            return response;
        }

        submitData.setUser(qUser);
        System.out.println(submitData.toString());

        try{
            logRepository.save(submitData);
        }
        catch(ConstraintViolationException e){
            response.put("error", "missing required paramater(s)");
        }
        
        response.put("success!", "log saved");
        return response;
    }

    @RequestMapping(value="/logs/{logid}", method=RequestMethod.GET)
    public Optional<Log> getLogbyID(@PathVariable long logid){
        return logRepository.findById(logid);
    }

    @RequestMapping(value="/logs", method=RequestMethod.GET)
    public Iterable<Log> getLogs(){
        return logRepository.findAll();

    }

    @RequestMapping(value="/logs/{logid}", method=RequestMethod.POST)
    public HashMap<String, String> updateLogById(@PathVariable long logid, @RequestBody Log updateData){
        HashMap<String,String> response =  new HashMap<String, String>();
        //logic is to find the id, then replace with new data
        Optional<Log> oldData = logRepository.findById(logid);
        if(oldData == null){
            response.put("error", "log id doesnt exist");
        }
        else{
            //check if new data provided
            if(!updateData.equals(oldData.orElse(null))){

                updateData.setid(oldData.get().getid());
                logRepository.save(updateData);
                response.put("success", "updated log");

            }
            else{
                response.put("unchanged","no new data was provided");
            }
        }
        return response;

    }

    //this uses the find first and then delete approach
    @RequestMapping(value="/logs/delete/{logid}", method=RequestMethod.POST)
    public HashMap<String, String> deleteLogbyID(@PathVariable long logid, @RequestBody Log logData){
        HashMap<String,String> response = new HashMap<String, String>();

        Optional<Log> result = logRepository.findById(logid);
        if(result.orElse(null) == null){
            response.put("nothing changed", "log id doesnt exist");
        }
        else{
            logRepository.deleteById(logid);
            response.put("success", "log was deleted");

        }
        
        return response;
    }


}