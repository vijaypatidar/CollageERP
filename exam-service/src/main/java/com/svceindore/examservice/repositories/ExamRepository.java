package com.svceindore.examservice.repositories;

import com.svceindore.examservice.models.ExamDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 3:42 PM
 **/

public interface ExamRepository extends MongoRepository<ExamDetail,String> {

    List<ExamDetail> findAllByCourseId(String courseId);

    List<ExamDetail> findAllByCourseIdAndBranchId(String courseId, String branchId);

    List<ExamDetail> findAllBySessionId(String sessionId);

    List<ExamDetail> findAllByCourseIdAndSessionId(String courseId, String sessionId);

    List<ExamDetail> findAllByCourseIdAndBranchIdAndSessionId(String courseId, String branchId, String sessionId);

    List<ExamDetail> findAllBySessionIdAndBranchId(String sessionId, String branchId);
}
