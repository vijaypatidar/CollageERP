package com.svceindore.courseservice.repositories;

import com.svceindore.courseservice.models.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 11/03/21
 * Time: 10:44 AM
 **/

public interface SubjectRepository extends MongoRepository<Subject,String> {
    List<Subject> findAllByCourseId(String courseId);
}
