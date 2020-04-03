package com.timelog.timelog.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil{

    @Autowired
    private SecurityConstants constants;


    //get the username fromt he JWT token passed in
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
   //find out how to get the stored token reference
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(constants.tokenSecret).parseClaimsJws(token).getBody();
    }
    
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());

    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<String,Object>();
        return createToken(claims, userDetails.getUsername());
    }

    //generate token based on claims and username
    //token is onyl generated after user is authenticated
    public String createToken(Map<String, Object> claims, String username){
        return Jwts.builder().setClaims(claims).setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10))
            .signWith(SignatureAlgorithm.HS256, constants.tokenSecret).compact();
    }

    //check if token is still valid (time)
    public Boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return ((username.equals(userDetails.getUsername())) && !isTokenExpired(token));
    }
    
}

