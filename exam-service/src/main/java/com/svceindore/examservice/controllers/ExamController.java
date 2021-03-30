package com.svceindore.examservice.controllers;

import com.svceindore.examservice.configs.Roles;
import com.svceindore.examservice.models.ExamDetail;
import com.svceindore.examservice.models.rest.Enrolled;
import com.svceindore.examservice.repositories.ExamRepository;
import net.minidev.json.JSONObject;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 3:39 PM
 **/
@RestController
public class ExamController {

    private final KeycloakRestTemplate restTemplate;
    private final ExamRepository examRepository;
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
    private final MongoTemplate mongoTemplate;
    public ExamController(KeycloakRestTemplate restTemplate, ExamRepository examRepository, MongoTemplate mongoTemplate) {
        this.restTemplate = restTemplate;
        this.examRepository = examRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @RolesAllowed(Roles.ROLE_FACULTY)
    @PostMapping("/exam")
    public ResponseEntity<?> createExam(@RequestBody ExamDetail examDetail) {
        JSONObject res = new JSONObject();
        logger.info(examDetail.toString());
        if (examDetail.getTitle() == null || examDetail.getTitle().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "Title required.");
            return ResponseEntity.ok(res.toString());
        }
        if (examDetail.getSubjectId() == null || examDetail.getSubjectId().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "subjectId required.");
            return ResponseEntity.ok(res.toString());
        }
        if (examDetail.getCourseId() == null || examDetail.getCourseId().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "Valid courseId required.");
            return ResponseEntity.ok(res.toString());
        }

        if (examDetail.getBranchId() == null || examDetail.getBranchId().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "Valid branchId required.");
            return ResponseEntity.ok(res.toString());
        }

        if (examDetail.getTotalMark() <= 0) {
            res.appendField("status", false);
            res.appendField("message", "Mark should must be greater than 0.");
            return ResponseEntity.ok(res.toString());
        }

        if (examDetail.getDuration() <= 0) {
            res.appendField("status", false);
            res.appendField("message", "Duration should must be greater than 0.");
            return ResponseEntity.ok(res.toString());
        }

        examRepository.insert(examDetail);

        res.appendField("status", true);
        res.appendField("message", "Exam created successfully.");
        res.appendField("id", examDetail.getId());
        return ResponseEntity.ok(res.toJSONString());
    }

    @RolesAllowed(Roles.ROLE_FACULTY)
    @PutMapping("/exam")
    public ResponseEntity<?> updateExam(@RequestBody ExamDetail examDetail) {
        JSONObject res = new JSONObject();
        logger.info(examDetail.toString());
        if (!examRepository.findById(examDetail.getId()).isPresent()) {
            res.appendField("status", false);
            res.appendField("message", "Exam detail not found with id=" + examDetail.getId());
            return ResponseEntity.ok(res.toString());
        }
        if (examDetail.getTitle() == null || examDetail.getTitle().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "Title required.");
            return ResponseEntity.ok(res.toString());
        }
        if (examDetail.getSubjectId() == null || examDetail.getSubjectId().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "subjectId required.");
            return ResponseEntity.ok(res.toString());
        }
        if (examDetail.getCourseId() == null || examDetail.getCourseId().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "Valid courseId required.");
            return ResponseEntity.ok(res.toString());
        }

        if (examDetail.getBranchId() == null || examDetail.getBranchId().isEmpty()) {
            res.appendField("status", false);
            res.appendField("message", "Valid branchId required.");
            return ResponseEntity.ok(res.toString());
        }

        if (examDetail.getTotalMark() <= 0) {
            res.appendField("status", false);
            res.appendField("message", "Mark should must be greater than 0.");
            return ResponseEntity.ok(res.toString());
        }
        if (examDetail.getDuration() <= 0) {
            res.appendField("status", false);
            res.appendField("message", "Duration should must be greater than 0.");
            return ResponseEntity.ok(res.toString());
        }

        examRepository.save(examDetail);

        res.appendField("status", true);
        res.appendField("message", "Exam detail updated successfully.");
        res.appendField("id", examDetail.getId());
        return ResponseEntity.ok(res.toJSONString());
    }

    @GetMapping("/exam/{id}")
    public ExamDetail getExamDetail(@PathVariable String id) {
        return examRepository.findById(id).get();
    }

    @GetMapping("/exams")
    public List<ExamDetail> getAllExamDetail(@RequestParam(required = false, defaultValue = "") String courseId,
                                             @RequestParam(required = false, defaultValue = "") String branchId,
                                             @RequestParam(required = false, defaultValue = "") String sessionId) {
        if (sessionId.isEmpty()) {
            if (courseId.isEmpty()) {
                return examRepository.findAll();
            } else {
                if (branchId.isEmpty()) {
                    return examRepository.findAllByCourseId(courseId);
                } else {
                    return examRepository.findAllByCourseIdAndBranchId(courseId, branchId);
                }
            }
        } else {
            if (courseId.isEmpty()) {
                return examRepository.findAllBySessionId(sessionId);
            } else {
                if (branchId.isEmpty()) {
                    return examRepository.findAllByCourseIdAndSessionId(courseId, sessionId);
                } else {
                    return examRepository.findAllByCourseIdAndBranchIdAndSessionId(courseId, branchId, sessionId);
                }
            }
        }
    }

    @GetMapping("/my-exams")
    public List<ExamDetail> getAllExamDetailForEnrolledCourse(@RequestParam(required = false,defaultValue = "-1") int semester) {
        List<ExamDetail> examDetails = new ArrayList<>();
        Enrolled[] enrolls = restTemplate.getForEntity(
                "lb://course-service/api/course/self-enrolls", Enrolled[].class
        ).getBody();
        if (enrolls != null) for (Enrolled e : enrolls) {
            Query query = new Query();
            Criteria criteria = Criteria.where("sessionId").is(e.getSessionId())
                    .and("branchId").is(e.getBranchId());

            if (semester!=-1)criteria.and("semester").is(semester);
            query.addCriteria(criteria);
            examDetails.addAll(mongoTemplate.find(query, ExamDetail.class));
        }

        return examDetails;
    }

}
