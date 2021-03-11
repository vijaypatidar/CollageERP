package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Enrolled;
import com.svceindore.courseservice.models.User;
import com.svceindore.courseservice.repositories.BranchRepository;
import com.svceindore.courseservice.repositories.CourseRepository;
import com.svceindore.courseservice.repositories.EnrolledRepository;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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

    public EnrollmentController(CourseRepository courseRepository, BranchRepository branchRepository, EnrolledRepository enrolledRepository, KeycloakRestTemplate restTemplate) {
        this.courseRepository = courseRepository;
        this.branchRepository = branchRepository;
        this.enrolledRepository = enrolledRepository;
        this.restTemplate = restTemplate;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/enrollStudent")
    public ResponseEntity<?> enrollStudent(@RequestBody Enrolled enrolled) throws JSONException {
        System.out.println(enrolled);
        JSONObject res = new JSONObject();


        if (enrolled.getCourseId() != null && courseRepository.findById(enrolled.getCourseId()).isPresent()) {

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

        } else {
            res.accumulate("status", false);
            res.accumulate("message", "Course not found with courseId=" + enrolled.getCourseId());
            return ResponseEntity.ok(res.toString());
        }
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @GetMapping("/enrolledStudents")
    public List<Enrolled> getEnrolledStudents(@RequestParam(required = false, defaultValue = "") String courseId,
                                              @RequestParam(required = false, defaultValue = "") String branchId,
                                              @RequestParam(required = false, defaultValue = "") String sessionId) {
        if (sessionId.isEmpty()) {
            if (courseId.isEmpty()) {
                return enrolledRepository.findAll();
            } else {
                if (branchId.isEmpty()) {
                    return enrolledRepository.findAllByCourseId(courseId);
                } else {
                    return enrolledRepository.findAllByCourseIdAndBranchId(courseId, branchId);
                }
            }
        } else {
            if (courseId.isEmpty()) {
                return enrolledRepository.findAllBySessionId(sessionId);
            } else {
                if (branchId.isEmpty()) {
                    return enrolledRepository.findAllByCourseIdAndSessionId(courseId, sessionId);
                } else {
                    return enrolledRepository.findAllByCourseIdAndBranchIdAndSessionId(courseId, branchId, sessionId);
                }
            }
        }
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
