package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.timetables.TimeTable;
import com.svceindore.courseservice.repositories.TimeTableRepository;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.List;

/**
 * Date: 16/03/21
 * Time: 7:35 AM
 **/
@RestController
public class TimeTableController {
    private final TimeTableRepository timeTableRepository;
    private final MongoTemplate mongoTemplate;

    public TimeTableController(TimeTableRepository timeTableRepository, MongoTemplate mongoTemplate) {
        this.timeTableRepository = timeTableRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/timeTable")
    public ResponseEntity<?> addTimeTable(@RequestBody TimeTable timeTable) throws JSONException {
        System.out.println(timeTable.toString());
        JSONObject res = new JSONObject();

        if (timeTable.getCourseId() == null || timeTable.getCourseId().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Course id require.");
            return ResponseEntity.ok(res.toString());
        }

        if (timeTable.getBranchId() == null || timeTable.getBranchId().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Branch id require.");
            return ResponseEntity.ok(res.toString());
        }

        if (timeTable.getSessionId() == null || timeTable.getSessionId().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Session id require.");
            return ResponseEntity.ok(res.toString());
        }

        Query query = new Query();
        query.addCriteria(
                Criteria.where("courseId").is(timeTable.getCourseId())
                        .and("branchId").is(timeTable.getBranchId())
                        .and("sessionId").is(timeTable.getSessionId())
        );

        List<TimeTable> timeTables = mongoTemplate.find(query, TimeTable.class);

        timeTable.setLastUpdatedOn(new Date());
        if (timeTables.isEmpty()){
            timeTableRepository.insert(timeTable);
            res.accumulate("message", "Time table saved successfully.");
        }else {
            timeTable.setId(timeTables.get(0).getId());
            timeTableRepository.save(timeTable);
            res.accumulate("message", "Time table updated successfully.");
        }

        res.accumulate("status", true);
        res.accumulate("id", timeTable.getId());
        return ResponseEntity.ok(res.toString());
    }

    @GetMapping("/timeTable")
    public TimeTable getTimeTable(
            @RequestParam String courseId,
            @RequestParam String branchId,
            @RequestParam String sessionId
    ){
        Query query = new Query();
        query.addCriteria(
                Criteria.where("courseId").is(courseId)
                        .and("branchId").is(branchId)
                        .and("sessionId").is(sessionId)
        );

        List<TimeTable> timeTables = mongoTemplate.find(query, TimeTable.class);

        return timeTables.get(0);
    }
}
