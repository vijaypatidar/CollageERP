package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Branch;
import com.svceindore.courseservice.models.Course;
import com.svceindore.courseservice.repositories.BranchRepository;
import com.svceindore.courseservice.repositories.CourseRepository;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

/**
 * Created by Vijay Patidar
 * Date: 27/02/21
 * Time: 9:53 PM
 **/
@RestController
public class BranchController {

    private final BranchRepository branchRepository;
    private final CourseRepository courseRepository;

    public BranchController(BranchRepository branchRepository, CourseRepository courseRepository) {
        this.branchRepository = branchRepository;
        this.courseRepository = courseRepository;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/addNewBranch")
    public ResponseEntity<?> createCourse(@RequestBody Branch branch) throws JSONException {
        JSONObject res = new JSONObject();
        if (branch.getName() == null || branch.getName().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Branch name required!");
            return ResponseEntity.ok(res);
        }

        if (branch.getCourseId() != null && courseRepository.findById(branch.getCourseId()).isPresent()) {
            res.accumulate("status", true);
            res.accumulate("message", "New branch added.");
            res.accumulate("id", branch.getId());
            branchRepository.insert(branch);
            return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
        } else {
            res.accumulate("status", false);
            res.accumulate("message", "Course not found with courseId=" + branch.getCourseId());
            return ResponseEntity.ok(res.toString());
        }
    }

    @DeleteMapping("/deleteBranch/{branchId}")
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
}
