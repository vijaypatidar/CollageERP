package com.svceindore.courseservice.repositories;

import com.svceindore.courseservice.models.Course;
import com.svceindore.courseservice.models.Enrolled;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Vijay Patidar
 * Date: 27/02/21
 * Time: 5:00 PM
 **/
public interface EnrolledRepository extends MongoRepository<Enrolled,String> {
}