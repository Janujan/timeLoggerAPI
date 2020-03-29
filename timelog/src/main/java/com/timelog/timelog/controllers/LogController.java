package com.timelog.timelog.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public HashMap<String,String> submitLog(@PathVariable final String username, @RequestBody final Log submitData){

        final HashMap<String, String> response = new HashMap<String,String>();

        final User qUser = userRepository.findByUsername(username);
    

        if(qUser == null){
            response.put("error", "user doesnt exist, register first");
            return response;
        }

        submitData.setUser(qUser);
        System.out.println(submitData.toString());

        try{
            logRepository.save(submitData);
        }
        catch(final ConstraintViolationException e){
            response.put("error", "missing required paramater(s)");
        }

        response.put("success!", "log saved");
        return response;
    }

    @RequestMapping(value="/logs/{username}/{logid}", method=RequestMethod.GET)
    public Optional<Log> getLogbyID(@PathVariable final long logid){
        return logRepository.findById(logid);
    }

    @RequestMapping(value="/logs/{username}", method=RequestMethod.GET)
    public Iterable<Log> getLogs(@PathVariable final String username){
        final User qUser = userRepository.findByUsername(username);
        List<Log> result = new ArrayList<Log>();
        if(qUser ==  null){
            return result;
        }
        result = logRepository.findAllLogsByUser(qUser.getId());

        return result;

    }

    // Create custom query for getting logs from a specific user
    @RequestMapping(value="/logs/{username}/{logid}", method=RequestMethod.POST)
    public HashMap<String, String> updateLogById(@PathVariable final String username, @PathVariable final long logid, @RequestBody final Log updateData){
        final HashMap<String,String> response =  new HashMap<String, String>();
        //logic is to find the id, then replace with new data

        //Find USer
        final User qUser = userRepository.findByUsername(username);
        if(qUser ==  null){
            response.put("error", "user doesnt exist, please register first");
            return response;
        }

        //Find logs by the user if found
        final Log oldData = logRepository.findLogByUser(qUser.getId(), logid);
        if(oldData == null){
            response.put("error", "log id doesnt exist");
        }
        else{

            //check if new data provided
            if(!updateData.equals(oldData)){

                updateData.setid(oldData.getid());
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
    @RequestMapping(value="/logs/delete/{username}/{logid}", method=RequestMethod.DELETE)
    public HashMap<String, String> deleteLogbyID(@PathVariable final String username, @PathVariable final long logid){
        final HashMap<String,String> response = new HashMap<String, String>();

        final User qUser = userRepository.findByUsername(username);
        if(qUser ==  null){
            response.put("error", "user doesnt exist, please register first");
            return response;
        }

        final Log result = logRepository.findLogByUser(qUser.getId(), logid);
        if(result == null){
            response.put("nothing changed", "log id doesnt exist");
        }
        else{
            logRepository.deleteById(logid);
            response.put("success", "log was deleted");

        }
        
        return response;
    }


}