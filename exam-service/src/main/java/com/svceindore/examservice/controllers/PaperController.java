package com.svceindore.examservice.controllers;

import com.svceindore.examservice.configs.Roles;
import com.svceindore.examservice.models.ExamDetail;
import com.svceindore.examservice.models.Paper;
import com.svceindore.examservice.repositories.ExamRepository;
import com.svceindore.examservice.repositories.PaperRepository;
import net.minidev.json.JSONObject;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Date: 14/03/21
 * Time: 8:15 PM
 **/
@RequestMapping("/papers")
@RestController
public class PaperController {

    private final KeycloakRestTemplate restTemplate;
    private final ExamRepository examRepository;
    private final PaperRepository paperRepository;
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    public PaperController(KeycloakRestTemplate restTemplate, ExamRepository examRepository, PaperRepository paperRepository) {
        this.restTemplate = restTemplate;
        this.examRepository = examRepository;
        this.paperRepository = paperRepository;
    }

    @RolesAllowed(Roles.ADMIN_ROLE)
    @PostMapping("/paper")
    public ResponseEntity<?> newPaper(@RequestBody Paper paper) {
        return createOrUpdatePaper(paper, true);
    }

    @RolesAllowed(Roles.ADMIN_ROLE)
    @PutMapping("/paper")
    public ResponseEntity<?> updatePaper(@RequestBody Paper paper) {
        return createOrUpdatePaper(paper, false);
    }

    @GetMapping("/paper/{paperId}")
    public ResponseEntity<?> getPaper(@PathVariable String paperId){
         ExamDetail examDetail = examRepository.findById(paperId).get();

         if (examDetail.getScheduledOn().getTime()<=new Date().getTime()){
            return ResponseEntity.ok(paperRepository.findById(paperId).get());
         }else {
             JSONObject res = new JSONObject();
             res.appendField("status",false);
             res.appendField("message","Paper not started yet.");
             return ResponseEntity.status(HttpStatus.ACCEPTED).body(res.toJSONString());
         }
    }

    public ResponseEntity<?> createOrUpdatePaper(Paper paper, boolean create) {
        JSONObject res = new JSONObject();
        if (isNullOrEmpty(paper.getId())) {
            res.appendField("status", false);
            res.appendField("message", "Paper id required.");
            return ResponseEntity.ok(res.toJSONString());
        }

        if (paper.getQuestions().size() == 0) {
            res.appendField("status", false);
            res.appendField("message", "Paper must contain at least one question.");
            return ResponseEntity.ok(res.toJSONString());
        }

        Optional<ExamDetail> optionalExamDetail = examRepository.findById(paper.getId());
        if (optionalExamDetail.isPresent() && optionalExamDetail.get().isOnlineMode()) {
            if (create && paperRepository.findById(paper.getId()).isPresent()) {
                res.appendField("status", false);
                res.appendField("message", "Paper already exists with this id.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(res.toJSONString());
            } else {
                paperRepository.save(paper);
                res.appendField("status", true);
                res.appendField("message", "Paper saved successfully");
                res.appendField("id", paper.getId());
                return ResponseEntity.status(create ? HttpStatus.CREATED : HttpStatus.OK)
                        .body(res.toJSONString());
            }
        } else if (!optionalExamDetail.isPresent()) {
            res.appendField("status", false);
            res.appendField("message", "Exam detail not found.");
            return ResponseEntity.ok(res.toJSONString());
        } else {
            res.appendField("status", false);
            res.appendField("message", "You can not create paper for offline exam.");
            return ResponseEntity.ok(res.toJSONString());
        }
    }

    public boolean isNullOrEmpty(String data) {
        return data == null || data.isEmpty();
    }
}