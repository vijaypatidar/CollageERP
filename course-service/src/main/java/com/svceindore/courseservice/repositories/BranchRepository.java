package com.svceindore.courseservice.repositories;

import com.svceindore.courseservice.models.Branch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 27/02/21
 * Time: 5:00 PM
 **/
public interface BranchRepository extends MongoRepository<Branch,String> {
    List<Branch> findAllByCourseId(String courseId);
}
