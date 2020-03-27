package com.timelog.timelog.models;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    public Iterable<Log> findAllLogsByUserid(long userId);
    // public Iterable<Log> findByIdAndUserid(long id, long userid);

	public Iterable<Log> findByIdAndUserid(long logid, long userId);
}