package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.models.Enrolled;
import com.svceindore.courseservice.repositories.BranchRepository;
import com.svceindore.courseservice.repositories.CourseRepository;
import com.svceindore.courseservice.repositories.EnrolledRepository;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    public EnrollmentController(CourseRepository courseRepository, BranchRepository branchRepository, EnrolledRepository enrolledRepository) {
        this.courseRepository = courseRepository;
        this.branchRepository = branchRepository;
        this.enrolledRepository = enrolledRepository;
    }

    @PostMapping("/enrollStudent")
    public ResponseEntity<?> enrollStudent(@RequestBody Enrolled enrolled) throws JSONException {
        System.out.println(enrolled);
        JSONObject res = new JSONObject();

        if (enrolled.getCourseId() != null && courseRepository.findById(enrolled.getCourseId()).isPresent()) {

            if (enrolled.getBranchId() != null && branchRepository.findById(enrolled.getBranchId()).isPresent()) {

                enrolledRepository.insert(enrolled);
                res.accumulate("status",true);
                res.accumulate("message","Enrolled successfully");
                res.accumulate("id",enrolled.getId());
                return ResponseEntity.ok(res.toString());
            }else {
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
}
