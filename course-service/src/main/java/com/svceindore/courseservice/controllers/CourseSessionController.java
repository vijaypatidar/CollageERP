package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Roles;
import com.svceindore.courseservice.models.Course;
import com.svceindore.courseservice.models.Session;
import com.svceindore.courseservice.repositories.SessionRepository;
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
 * Date: 10/03/21
 * Time: 1:53 PM
 **/
@RestController
public class CourseSessionController {

    private final SessionRepository sessionRepository;

    public CourseSessionController(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody Session session) throws JSONException {
        JSONObject res = new JSONObject();

        if (session.getName() == null || session.getName().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Session name required!");
            return ResponseEntity.ok(res.toString());
        }

        sessionRepository.insert(session);

        res.accumulate("status", true);
        res.accumulate("message", "New session added.");
        res.accumulate("id", session.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }

    @RolesAllowed(Roles.ROLE_ADMIN)
    @PutMapping("/session")
    public ResponseEntity<?> updateSession(@RequestBody Session session) throws JSONException {
        JSONObject res = new JSONObject();

        if (session.getName() == null || session.getName().isEmpty()) {
            res.accumulate("status", false);
            res.accumulate("message", "Session name required!");
            return ResponseEntity.ok(res.toString());
        }

        sessionRepository.save(session);

        res.accumulate("status", true);
        res.accumulate("message", "New session added.");
        res.accumulate("id", session.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }

    @GetMapping("/session")
    public List<Session> getAllSession() {
        return sessionRepository.findAll();
    }

    @GetMapping("/session/{sessionId}")
    public Session getSession(@PathVariable String sessionId) {
        return sessionRepository.findById(sessionId).get();
    }


}
