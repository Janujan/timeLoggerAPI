package com.timelog.timelog.models;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
    public User findByUsername(String username);
    public User findByEmail(String email);
    public User findByUsernameOrEmail(String username, String Email);
}