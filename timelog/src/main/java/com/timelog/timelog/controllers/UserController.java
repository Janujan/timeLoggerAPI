package com.timelog.timelog.controllers;

import java.util.HashMap;

import com.timelog.timelog.models.JWTResponse;
import com.timelog.timelog.models.User;
import com.timelog.timelog.models.UserCredentials;
import com.timelog.timelog.models.UserRepository;
import com.timelog.timelog.services.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController{

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwt;

    @Autowired
    BCryptPasswordEncoder encoder;

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

            //rehash the passowrd
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            response.put("id", Long.toString(user.getId()));
            return response;
        }
        else{
            return response;
        }

    }
    @RequestMapping(value="/authenticate", method=RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody UserCredentials userCred) throws Exception{
        try{
            authManager.authenticate(new UsernamePasswordAuthenticationToken(userCred.getUsername(), userCred.getPassword()));
        }
        catch(BadCredentialsException e){
            throw new Exception("incorrect username or password", e);
        }
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(userCred.getUsername());
        
        String token = jwt.generateToken(userDetails);

        return ResponseEntity.ok(new JWTResponse(token));
    }
}