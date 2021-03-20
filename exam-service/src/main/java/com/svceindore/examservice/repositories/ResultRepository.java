package com.svceindore.examservice.repositories;

import com.svceindore.examservice.models.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 3:42 PM
 **/
public interface ResultRepository extends MongoRepository<Result, String> {

    void deleteByExamId(String examId);
}
