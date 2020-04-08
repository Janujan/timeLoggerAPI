package com.timelog.timelog.controllers;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import com.timelog.timelog.models.Log;
import com.timelog.timelog.models.LogRepository;
import com.timelog.timelog.models.User;
import com.timelog.timelog.models.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //This function will now get user based on user id passed in via path variable
    //Then it will add the log under that user.
    @RequestMapping(value="/logs/submit", method=RequestMethod.POST)
    public ResponseEntity<?> submitLog(Principal principal, @RequestBody final Log submitData){


        final User qUser = userRepository.findByUsername(principal.getName());

        submitData.setUser(qUser);
        System.out.println(submitData.toString());

        try{
            logRepository.save(submitData);
        }
        catch(final ConstraintViolationException e){
            System.out.println("log not saved");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value="/logs/{logid}", method=RequestMethod.GET)
    public Log getLogbyID(Principal principal, @PathVariable final long logid){
        User qUser = userRepository.findByUsername(principal.getName());

        Log log = logRepository.findLogByUser(qUser.getId(), logid);
        return log;
    }

    @RequestMapping(value="/logs/", method=RequestMethod.GET)
    public Iterable<Log> getLogs(Principal principal){
        final User qUser = userRepository.findByUsername(principal.getName());
        List<Log> result = new ArrayList<Log>();
        if(qUser ==  null){
            return result;
        }
        result = logRepository.findAllLogsByUser(qUser.getId());

        return result;
    }

    // Create custom query for getting logs from a specific user
    @RequestMapping(value="/logs/{logid}", method=RequestMethod.POST)
    public ResponseEntity<?> updateLogById(Principal principal, @PathVariable final long logid, @RequestBody final Log updateData){
        //get User
        final User qUser = userRepository.findByUsername(principal.getName());

        //Find logs by the user if found
        final Log oldData = logRepository.findLogByUser(qUser.getId(), logid);
        
        if(oldData == null){
            System.out.println("not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{

            //check if new data provided
            if(!updateData.equals(oldData)){

                updateData.setid(oldData.getid());
                updateData.setUser(qUser);
                logRepository.save(updateData);
                System.out.println("Updated log");
                System.out.println(updateData.toString());
            }
            else{
                System.out.println("Log not updated");
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);   
            }

        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //this uses the find first and then delete approach
    @RequestMapping(value="/logs/{logid}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteLogbyID(Principal principal, @PathVariable final long logid){

        final User qUser = userRepository.findByUsername(principal.getName());

        final Log result = logRepository.findLogByUser(qUser.getId(), logid);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            logRepository.deleteById(logid);
        }
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}