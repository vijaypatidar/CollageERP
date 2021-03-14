package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Subject;
import com.svceindore.courseservice.repositories.CourseRepository;
import com.svceindore.courseservice.repositories.SubjectRepository;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 11/03/21
 * Time: 10:45 AM
 **/
@RestController
public class SubjectController {
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    public SubjectController(SubjectRepository subjectRepository, CourseRepository courseRepository) {
        this.subjectRepository = subjectRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping(value = "/subject", params = "courseId")
    public List<Subject> getSubjectByCourseId(@RequestParam String courseId) {
        return subjectRepository.findAllByCourseId(courseId);
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/subject")
    public ResponseEntity<?> addSubject(@RequestBody Subject subject) throws JSONException {
        JSONObject res = new JSONObject();
        res.accumulate("status", false);

        if (subject.getName() == null || subject.getName().isEmpty()) {
            res.accumulate("message", "Valid name require.");
            return ResponseEntity.ok(res.toString());
        }
        if (subject.getId() == null || subject.getId().isEmpty()) {
            res.accumulate("message", "Subject code require.");
            return ResponseEntity.ok(res.toString());
        }
        if (subject.getCourseId() == null || subject.getCourseId().isEmpty()) {
            res.accumulate("message", "Valid course id require.");
            return ResponseEntity.ok(res.toString());
        }

        if (subjectRepository.findById(subject.getId()).isPresent()) {
            res.accumulate("message", "Subject already exists with same subject code.");
            return ResponseEntity.ok(res.toString());
        }

        if (!courseRepository.findById(subject.getCourseId()).isPresent()) {
            res.accumulate("message", "Invalid course id.");
            return ResponseEntity.ok(res.toString());
        }

        subjectRepository.save(subject);

        res.accumulate("status", true);
        res.accumulate("message", "Subject added");
        res.accumulate("id", subject.getId());

        return ResponseEntity.ok(res.toString());
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @DeleteMapping("/subject/{id}")
    public ResponseEntity<?> deleteSubject(@PathVariable String id) throws JSONException {
        subjectRepository.deleteById(id);
        JSONObject res = new JSONObject();
        res.accumulate("status", true);
        res.accumulate("message", "Subject deleted with code " + id);
        return ResponseEntity.ok(res.toString());
    }
}
