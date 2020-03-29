package com.timelog.timelog.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {

    @Query(value="SELECT * FROM log l where l.user = ?1", nativeQuery = true)
    List<Log> findAllLogsByUser( long userid );


    @Query(value="SELECT * from log l where l.user = ?1 and l.id = ?2", nativeQuery = true)
    Log findLogByUser(long userid, long logid);
}