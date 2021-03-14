package com.svceindore.examservice.repositories;

import com.svceindore.examservice.models.Paper;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Date: 14/03/21
 * Time: 8:10 PM
 **/
public interface PaperRepository extends MongoRepository<Paper,String> {

}
