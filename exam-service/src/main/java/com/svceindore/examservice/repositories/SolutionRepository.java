package com.svceindore.examservice.repositories;

import com.svceindore.examservice.models.Solution;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Date: 23/02/21
 * Time: 3:42 PM
 **/

public interface SolutionRepository extends MongoRepository<Solution,String> {

    Optional<Solution> findByPaperIdAndStudentId(String paperId, String name);
}
