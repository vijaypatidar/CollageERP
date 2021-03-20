package com.svceindore.examservice.controllers;

import com.svceindore.examservice.configs.Roles;
import com.svceindore.examservice.models.ExamDetail;
import com.svceindore.examservice.models.Paper;
import com.svceindore.examservice.models.Question;
import com.svceindore.examservice.models.Solution;
import com.svceindore.examservice.repositories.ExamRepository;
import com.svceindore.examservice.repositories.PaperRepository;
import com.svceindore.examservice.repositories.SolutionRepository;
import net.minidev.json.JSONObject;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.Collection;
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
    private final MongoTemplate mongoTemplate;
    private final SolutionRepository solutionRepository;
    private final Logger logger = Logger.getLogger(getClass().getSimpleName());

    public PaperController(KeycloakRestTemplate restTemplate, ExamRepository examRepository, PaperRepository paperRepository, MongoTemplate mongoTemplate, SolutionRepository solutionRepository) {
        this.restTemplate = restTemplate;
        this.examRepository = examRepository;
        this.paperRepository = paperRepository;
        this.mongoTemplate = mongoTemplate;
        this.solutionRepository = solutionRepository;
    }

    @RolesAllowed(Roles.ROLE_FACULTY)
    @PostMapping("/paper")
    public ResponseEntity<?> newPaper(@RequestBody Paper paper) {
        return createOrUpdatePaper(paper, true);
    }

    @RolesAllowed(Roles.ROLE_FACULTY)
    @PutMapping("/paper")
    public ResponseEntity<?> updatePaper(@RequestBody Paper paper) {
        return createOrUpdatePaper(paper, false);
    }

    @GetMapping("/paper/{paperId}")
    public ResponseEntity<?> getPaper(@PathVariable String paperId) {

        Optional<ExamDetail> optional = examRepository.findById(paperId);
        if (optional.isPresent()) {
            ExamDetail examDetail = optional.get();

            boolean isAdmin = false;


            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(Roles.ROLE_FACULTY)) {
                    isAdmin = true;
                    break;
                }
            }

            if (examDetail.getScheduledOn().getTime() <= new Date().getTime() || isAdmin) {

                Query query = new Query();
                query.fields().exclude("answers");
                query.addCriteria(Criteria.where("_id").is(paperId));
                Paper paper = mongoTemplate.findOne(query, Paper.class);

                return ResponseEntity.ok(paper);
            } else {
                JSONObject res = new JSONObject();
                res.appendField("status", false);
                res.appendField("message", "Paper not started yet.");
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(res.toJSONString());
            }

        } else {

            JSONObject res = new JSONObject();
            res.appendField("status", false);
            res.appendField("message", "Exam detail not found.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(res.toJSONString());

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

            int mark = paper.getQuestions().stream().mapToInt(Question::getMark).sum();
            if (mark!=optionalExamDetail.get().getTotalMark()){
                res.appendField("status", false);
                res.appendField("message", "Sum of mark is not equal to total mark declared in exam detail.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(res.toJSONString());
            }
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

    @PostMapping("/submit-exam/{paperId}")
    public ResponseEntity<?> submitExam(@PathVariable String paperId, @RequestBody Solution solution, Principal principal) {
        Optional<ExamDetail> optional = examRepository.findById(paperId);

        if (optional.isPresent()) {
            ExamDetail examDetail = optional.get();
            long sTime = examDetail.getScheduledOn().getTime();
            long eTime = sTime + examDetail.getDuration() * 60000L;
            long cTime = new Date().getTime();
            long submissionTime = eTime + 10 * 60 * 1000L;

            if (sTime <= cTime && cTime <= submissionTime) {
                if (solutionRepository.findByPaperIdAndStudentId(paperId, principal.getName()).isPresent()) {

                    JSONObject res = new JSONObject();
                    res.appendField("status", false);
                    res.appendField("message", "Solution already submitted.");
                    return ResponseEntity.ok(res.toJSONString());
                } else {
                    solution.setPaperId(paperId);
                    solution.setStudentId(principal.getName());
                    solutionRepository.insert(solution);

                    JSONObject res = new JSONObject();
                    res.appendField("status", true);
                    res.appendField("message", "Solution saved successfully.");
                    return ResponseEntity.ok(res.toJSONString());
                }
            } else if (sTime >= cTime) {
                JSONObject res = new JSONObject();
                res.appendField("status", false);
                res.appendField("message", "Paper not started yet.");
                logger.warning(principal.getName() + " is trying to submit paper before it started.");
                return ResponseEntity.ok(res.toJSONString());
            } else {
                JSONObject res = new JSONObject();
                res.appendField("status", false);
                res.appendField("message", "You are too late, you can not submit solution.");
                return ResponseEntity.ok(res.toJSONString());
            }
        } else {
            JSONObject res = new JSONObject();
            res.appendField("status", false);
            res.appendField("message", "Exam detail not found");
            return ResponseEntity.ok(res.toJSONString());
        }

    }


    @RolesAllowed(Roles.ROLE_FACULTY)
    @PostMapping("/submit-exam-solution/{paperId}")
    public ResponseEntity<?> submitExamSolution(@PathVariable String paperId, @RequestBody Solution solution, Principal principal) {
        Optional<ExamDetail> optional = examRepository.findById(paperId);

        if (optional.isPresent()) {
            Paper paper = paperRepository.findById(paperId).get();
            paper.setAnswers(solution.getAnswers());
            paperRepository.save(paper);

            JSONObject res = new JSONObject();
            res.appendField("status", true);
            res.appendField("message", "Solution saved successfully.");
            return ResponseEntity.ok(res.toJSONString());

        } else {
            JSONObject res = new JSONObject();
            res.appendField("status", false);
            res.appendField("message", "Exam detail not found");
            return ResponseEntity.ok(res.toJSONString());
        }

    }


    public boolean isNullOrEmpty(String data) {
        return data == null || data.isEmpty();
    }
}
