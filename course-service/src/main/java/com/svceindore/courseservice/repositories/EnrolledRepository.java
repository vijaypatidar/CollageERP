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
    List<Enrolled> findAllBySessionId(String sessionId);
    List<Enrolled> findAllByCourseId(String courseId);
    List<Enrolled> findAllByCourseIdAndSessionId(String courseId, String sessionId);

    List<Enrolled> findAllByCourseIdAndBranchId(String courseId, String branchId);

    List<Enrolled> findAllByCourseIdAndBranchIdAndSessionId(String courseId, String branchId, String sessionId);

    List<Enrolled> findAllByCourseIdAndBranchIdAndStudentUsername(String courseId, String branchId, String studentUsername);

    List<Enrolled> findAllByStudentUsername(String studentUsername);
}
