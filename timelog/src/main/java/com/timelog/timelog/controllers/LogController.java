package com.timelog.timelog.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import com.timelog.timelog.models.Log;
import com.timelog.timelog.models.LogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@RestController
public class LogController {

    @Autowired
    private LogRepository logRepository;


    @ResponseStatus(value = HttpStatus.OK, reason = "")
    @RequestMapping(value="/logs/submit/{userId}", method=RequestMethod.POST)
    public HashMap<String,String> submitLog(@RequestBody Log submitData, @PathVariable long userId){
        HashMap<String, String> response = new HashMap<String,String>();
        try{
            submitData.setUser(userId);
            logRepository.save(submitData);
        }
        catch(ConstraintViolationException e){
            response.put("error", "missing required paramater(s)");
        }
        response.put("success!", "log saved");
        return response;
    }

    @ResponseStatus(value = HttpStatus.OK, reason = "")
    @RequestMapping(value="/logs/{userId}/{logid}", method=RequestMethod.GET)
    public Iterable<Log> getLogbyID(@PathVariable long userId, @PathVariable long logid){
        //return logRepository.findAllLogsByUserid(userId);
        return logRepository.findByIdAndUserid(logid, userId);
    }

    @ResponseStatus(value = HttpStatus.OK, reason = "")
    @RequestMapping(value="/logs/{userId}", method=RequestMethod.GET)
    public Iterable<Log> getLogs(@PathVariable long userId){
        return logRepository.findAllLogsByUserid(userId);

    }

    @RequestMapping(value="/logs/{userId}/{logId}", method=RequestMethod.POST)
    public HashMap<String, String> updateLogById(@PathVariable long userId, @PathVariable long logId, @RequestBody Log updateData){
        HashMap<String,String> response =  new HashMap<String, String>();
        //logic is to find the id, then replace with new data
        Optional<Log> oldData = logRepository.findById(logId);
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
    public HashMap<String, String> deleteLogbyID(@PathVariable long logid, @RequestBody long userid){
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