package com.svceindore.libraryservice.repositories;

import com.svceindore.libraryservice.models.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by Vijay Patidar
 * Date: 29/01/21
 * Time: 10:19 AM
 **/

@Repository
public interface HistoryRepository extends MongoRepository<History,String> {
    History findByBidAndSubmittedOn(String bid, Date submittedOn);
}