package com.svceindore.examservice.repositories;

import com.svceindore.examservice.models.ExamDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 3:42 PM
 **/

public interface ExamRepository extends MongoRepository<ExamDetail,String> {

}
