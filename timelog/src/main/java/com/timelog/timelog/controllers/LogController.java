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
    @RequestMapping(value="/logs/submit", method=RequestMethod.POST)
    public HashMap<String,String> submitLog(@RequestBody Log submitData){
        HashMap<String, String> response = new HashMap<String,String>();
        try{
            logRepository.save(submitData);
        }
        catch(ConstraintViolationException e){
            response.put("error", "missing required paramater(s)");
        }
        response.put("success!", "log saved");
        return response;
    }

    @ResponseStatus(value = HttpStatus.OK, reason = "")
    @RequestMapping(value="/logs/{logid}", method=RequestMethod.GET)
    public Optional<Log> getLogbyID(@PathVariable long logid){
        return logRepository.findById(logid);
    }

    @ResponseStatus(value = HttpStatus.OK, reason = "")
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