package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Branch;
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
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 27/02/21
 * Time: 9:53 PM
 **/
@RestController
public class BranchController {

    private final BranchRepository branchRepository;
    private final CourseRepository courseRepository;
    private final EnrolledRepository enrolledRepository;

    public BranchController(BranchRepository branchRepository, CourseRepository courseRepository, EnrolledRepository enrolledRepository) {
        this.branchRepository = branchRepository;
        this.courseRepository = courseRepository;
        this.enrolledRepository = enrolledRepository;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/branch")
    public ResponseEntity<?> createBranch(@RequestBody Branch branch) throws JSONException {
        JSONObject res = new JSONObject();
        if (branch.getName() == null || branch.getName().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Branch name required!");
            return ResponseEntity.ok(res.toString());
        }

        if (branchRepository.findById(branch.getId()).isPresent()){
            res.accumulate("status", false);
            res.accumulate("message", "Branch already exists with same id. id="+branch.getId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res.toString());
        }

        if (branch.getCourseId() != null && courseRepository.findById(branch.getCourseId()).isPresent()) {
            res.accumulate("status", true);
            res.accumulate("message", "New branch added.");
            res.accumulate("id", branch.getId());
            branchRepository.save(branch);
            return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
        } else {
            res.accumulate("status", false);
            res.accumulate("message", "Course not found with courseId=" + branch.getCourseId());
            return ResponseEntity.ok(res.toString());
        }
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PutMapping("/branch")
    public ResponseEntity<?> updateBranch(@RequestBody Branch branch) throws JSONException {
        JSONObject res = new JSONObject();

        if (branch.getName() == null || branch.getName().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Branch name required!");
            return ResponseEntity.ok(res.toString());
        }


        if (!branchRepository.findById(branch.getId()).isPresent()){
            res.accumulate("status", false);
            res.accumulate("message", "Branch not found with branchId = "+branch.getId());
            return ResponseEntity.ok(res.toString());
        }

        if (branch.getCourseId() != null && courseRepository.findById(branch.getCourseId()).isPresent()) {
            res.accumulate("status", true);
            res.accumulate("message", "Branch updated successfully.");
            res.accumulate("id", branch.getId());
            branchRepository.save(branch);
            return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
        } else {
            res.accumulate("status", false);
            res.accumulate("message", "Course not found with courseId=" + branch.getCourseId());
            return ResponseEntity.ok(res.toString());
        }
    }

    @GetMapping("/enrolled/branch")
    public List<Branch> getBranchForEnrolledCourses(Principal principal) throws JSONException {
        List<Branch> res = new ArrayList<>();
        enrolledRepository.findAllByStudentUsername(principal.getName()).forEach(enrolled -> {
            res.add(branchRepository.findById(enrolled.getBranchId()).get());
        });
        return res;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @DeleteMapping("/branch/{branchId}")
    public ResponseEntity<?> deleteBranch(@PathVariable String branchId) throws JSONException {
        branchRepository.deleteById(branchId);
        JSONObject response = new JSONObject();
        response.accumulate("status", true);
        response.accumulate("message", "Branch deleted with id=" + branchId);
        return ResponseEntity.ok(response.toString());
    }

    @GetMapping("/getBranchList/{courseId}")
    public List<Branch> getBranchList(@PathVariable String courseId) {
        return branchRepository.findAllByCourseId(courseId);
    }

    @GetMapping("/branch/{id}")
    public Branch getBranchById(@PathVariable String id){
        return branchRepository.findById(id).get();
    }
}
