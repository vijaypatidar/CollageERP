package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Course;
import com.svceindore.courseservice.models.Enrolled;
import com.svceindore.courseservice.models.User;
import com.svceindore.courseservice.repositories.BranchRepository;
import com.svceindore.courseservice.repositories.CourseRepository;
import com.svceindore.courseservice.repositories.EnrolledRepository;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Vijay Patidar
 * Date: 01/03/21
 * Time: 7:32 AM
 **/
@RestController
public class EnrollmentController {
    private final CourseRepository courseRepository;
    private final BranchRepository branchRepository;
    private final EnrolledRepository enrolledRepository;
    private final KeycloakRestTemplate restTemplate;
    private final MongoTemplate mongoTemplate;

    public EnrollmentController(CourseRepository courseRepository, BranchRepository branchRepository, EnrolledRepository enrolledRepository, KeycloakRestTemplate restTemplate, MongoTemplate mongoTemplate) {
        this.courseRepository = courseRepository;
        this.branchRepository = branchRepository;
        this.enrolledRepository = enrolledRepository;
        this.restTemplate = restTemplate;
        this.mongoTemplate = mongoTemplate;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/enrollStudent")
    public ResponseEntity<?> enrollStudent(@RequestBody Enrolled enrolled) throws JSONException {
        System.out.println(enrolled);
        JSONObject res = new JSONObject();


        if (enrolled.getCourseId() != null) {

            Optional<Course> optional = courseRepository.findById(enrolled.getCourseId());
            if (optional.isPresent() && optional.get().isActive()) {
                if (enrolled.getBranchId() != null && branchRepository.findById(enrolled.getBranchId()).isPresent()) {
                    //checking for previous record for duplicate
                    List<Enrolled> enrolls = enrolledRepository.findAllByCourseIdAndBranchIdAndStudentUsername(
                            enrolled.getCourseId(),
                            enrolled.getBranchId(),
                            enrolled.getStudentUsername()
                    );

                    if (!enrolls.isEmpty()) {
                        res.accumulate("status", false);
                        res.accumulate("message", "Already enrolled in this course.");
                        res.accumulate("id", enrolls.get(0).getId());
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(res.toString());
                    }

                    ResponseEntity<User> entity = restTemplate.getForEntity("lb://user-service/api/user/profile/" + enrolled.getStudentUsername(), User.class);
                    User u = entity.getBody();
                    if (entity.getStatusCodeValue() == 200 && u != null) {
                        enrolled.setStudentName(u.getFirstName() + " " + u.getLastName());
                        enrolled.setEnrollmentDate(new Date());
                        enrolled.setCurrentSemester(1);
                        enrolledRepository.insert(enrolled);
                        res.accumulate("status", true);
                        res.accumulate("message", "Enrolled successfully");
                        res.accumulate("id", enrolled.getId());
                    } else {
                        res.accumulate("status", false);
                        res.accumulate("message", "Invalid studentUsername");
                    }
                    return ResponseEntity.ok(res.toString());
                } else {
                    res.accumulate("status", false);
                    res.accumulate("message", "Branch not found with id=" + enrolled.getBranchId());
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(res.toString());
                }
            } else if (!optional.isPresent()) {
                res.accumulate("status", false);
                res.accumulate("message", "Course not found with courseId=" + enrolled.getCourseId());
                return ResponseEntity.ok(res.toString());
            } else {
                res.accumulate("status", false);
                res.accumulate("message", "Course is not active.");
                return ResponseEntity.ok(res.toString());
            }


        } else {
            res.accumulate("status", false);
            res.accumulate("message", "Course id required.");
            return ResponseEntity.ok(res.toString());
        }
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @GetMapping("/enrolledStudents")
    public List<Enrolled> getEnrolledStudents(@RequestParam(required = false, defaultValue = "") String courseId,
                                              @RequestParam(required = false, defaultValue = "") String branchId,
                                              @RequestParam(required = false, defaultValue = "") String sessionId,
                                              @RequestParam(required = false, defaultValue = "-1") int semester) {
        Query query = new Query();
        Criteria criteria = new Criteria();

        if (!sessionId.isEmpty()) criteria.and("sessionId").is(sessionId);

        if (semester!=-1) criteria.and("currentSemester").is(semester);

        if (!courseId.isEmpty()) {
            criteria.and("courseId").is(courseId);
            if (!branchId.isEmpty())
                criteria.and("branchId").is(branchId);

        }
        query.addCriteria(criteria);
        return mongoTemplate.find(query, Enrolled.class);
    }

    @GetMapping("/self-enrolls")
    public List<Enrolled> getSelfEnrolls(Principal principal) {
        return enrolledRepository.findAllByStudentUsername(principal.getName());
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @GetMapping("/enrollDetail/{enrollId}")
    public ResponseEntity<?> getEnrollInfo(@PathVariable String enrollId) {
        Optional<Enrolled> optional = enrolledRepository.findById(enrollId);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
