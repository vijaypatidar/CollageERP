package com.svceindore.courseservice.repositories;

import com.svceindore.courseservice.models.Enrolled;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 27/02/21
 * Time: 5:00 PM
 **/
public interface EnrolledRepository extends MongoRepository<Enrolled,String> {
    List<Enrolled> findAllByCourseId(String courseId);

    List<Enrolled> findAllByCourseIdAndBranchId(String courseId, String branchId);

    List<Enrolled> findAllByCourseIdAndBranchIdAndStudentUsername(String courseId, String branchId, String studentUsername);
}
