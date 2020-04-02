package com.timelog.timelog.services;

import java.util.ArrayList;

import com.timelog.timelog.models.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LogUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.timelog.timelog.models.User u = userRepository.findByUsername(username);

        // last param is the granted roles which i havent made just yet
        return new User(u.getUsername(), u.getPassword(), new ArrayList<>());
    } 
        
}