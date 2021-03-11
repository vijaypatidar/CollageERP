package com.svceindore.courseservice.repositories;

import com.svceindore.courseservice.models.Session;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 10/03/21
 * Time: 2:04 PM
 **/
public interface SessionRepository extends MongoRepository<Session,String> {
    List<Session> findAllByCurrentSession(boolean currentSession);
}
