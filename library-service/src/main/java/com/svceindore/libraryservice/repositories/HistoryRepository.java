package com.svceindore.libraryservice.repositories;

import com.svceindore.libraryservice.models.History;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 29/01/21
 * Time: 10:19 AM
 **/

@Repository
public interface HistoryRepository extends MongoRepository<History,String> {
    History findByBcidAndSubmittedOn(String bid, Date submittedOn);

    List<History> findByIssuedTo(String issuedTo);
}
