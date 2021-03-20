package com.svceindore.courseservice.repositories;

import com.svceindore.courseservice.models.timetables.TimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Date: 27/02/21
 * Time: 5:00 PM
 **/
public interface TimeTableRepository extends MongoRepository<TimeTable,String> {

}
