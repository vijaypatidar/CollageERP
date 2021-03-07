package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Enrolled;
import com.svceindore.courseservice.repositories.BranchRepository;
import com.svceindore.courseservice.repositories.CourseRepository;
import com.svceindore.courseservice.repositories.EnrolledRepository;
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

    public EnrollmentController(CourseRepository courseRepository, BranchRepository branchRepository, EnrolledRepository enrolledRepository) {
        this.courseRepository = courseRepository;
        this.branchRepository = branchRepository;
        this.enrolledRepository = enrolledRepository;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/enrollStudent")
    public ResponseEntity<?> enrollStudent(@RequestBody Enrolled enrolled) throws JSONException {
        System.out.println(enrolled);
        JSONObject res = new JSONObject();

        if (enrolled.getCourseId() != null && courseRepository.findById(enrolled.getCourseId()).isPresent()) {

            if (enrolled.getBranchId() != null && branchRepository.findById(enrolled.getBranchId()).isPresent()) {

                enrolled.setEnrollmentDate(new Date());
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

    @RolesAllowed(Roles.ROLE_ADMIN)
    @GetMapping("/enrolledStudents")
    public List<Enrolled> getEnrolledStudents(@RequestParam(required = false,defaultValue = "") String courseId,
                                              @RequestParam(required = false,defaultValue = "") String branchId){
        if (courseId.isEmpty()){
            return enrolledRepository.findAll();
        }else {
            if (branchId.isEmpty()){
                return enrolledRepository.findAllByCourseId(courseId);
            }else {
                return enrolledRepository.findAllByCourseIdAndBranchId(courseId,branchId);
            }
        }
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @GetMapping("/enrollDetail/{enrollId}")
    public ResponseEntity<?> getEnrollInfo(@PathVariable String enrollId){
        Optional<Enrolled> optional = enrolledRepository.findById(enrollId);
        if (optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
