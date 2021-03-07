package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Course;
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
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/addNewCourse")
    public ResponseEntity<?> createCourse(@RequestBody Course course) throws JSONException {
        JSONObject res = new JSONObject();
        if (course.getName() == null || course.getName().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "course name required!");
            return ResponseEntity.ok(res.toString());
        }

        if (course.getDuration() <= 0) {
            res.accumulate("status", false);
            res.accumulate("message", "Course duration should be greater than or equal to 1.");
            return ResponseEntity.ok(res.toString());
        }
        if (course.getFeePerYear() < 0) {
            res.accumulate("status", false);
            res.accumulate("message", "Course fee per year should be greater than or equal to 0.");
            return ResponseEntity.ok(res.toString());
        }

        if (course.getId()==null||course.getId().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Course id required.");
            return ResponseEntity.ok(res.toString());
        }

        if (courseRepository.findById(course.getId()).isPresent()){
            res.accumulate("status", false);
            res.accumulate("message", "Course already exists with same id. id="+course.getId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res.toString());
        }

        courseRepository.save(course);

        res.accumulate("status", true);
        res.accumulate("message", "New course added.");
        res.accumulate("id", course.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PutMapping("/updateCourse")
    public ResponseEntity<?> updateCourse(@RequestBody Course course) throws JSONException {
        JSONObject res = new JSONObject();

        if (course.getId() == null || course.getId().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Valid course id required for updating course.");
            return ResponseEntity.ok(res.toString());
        }

        if (course.getName() == null || course.getName().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "course name required!");
            return ResponseEntity.ok(res.toString());
        }

        if (course.getDuration() <= 0) {
            res.accumulate("status", false);
            res.accumulate("message", "Course duration should be greater than or equal to 1.");
            return ResponseEntity.ok(res.toString());
        }

        if (course.getFeePerYear() < 0) {
            res.accumulate("status", false);
            res.accumulate("message", "Course fee per year should be greater than or equal to 0.");
            return ResponseEntity.ok(res.toString());
        }

        Optional<Course> optional = courseRepository.findById(course.getId());
        if (optional.isPresent()) {
            res.accumulate("status", true);
            res.accumulate("message", "Course detail updated.");
            res.accumulate("id", course.getId());
            courseRepository.save(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
        } else {
            res.accumulate("status", false);
            res.accumulate("message","Course not found for id="+course.getId());
            return ResponseEntity.ok(res.toString());
        }
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @DeleteMapping("/removeCourse/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable String courseId) throws JSONException {
        System.out.println("DELETE COURSE "+courseId);
        JSONObject response = new JSONObject();
        courseRepository.deleteById(courseId);
        response.accumulate("status",true);
        response.accumulate("message","Course deleted with id="+courseId);
        return ResponseEntity.ok(response.toString());
    }

    @GetMapping("/courseInfo/{courseId}")
    public Course getCourseInfo(@PathVariable String courseId){
        return courseRepository.findById(courseId).get();
    }

    @GetMapping("/getCourseList")
    public List<Course> getCourseList() {
        return courseRepository.findAll();
    }
}
