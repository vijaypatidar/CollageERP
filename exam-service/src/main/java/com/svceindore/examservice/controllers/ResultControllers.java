package com.svceindore.examservice.controllers;

import com.svceindore.examservice.configs.Roles;
import com.svceindore.examservice.models.*;
import com.svceindore.examservice.models.rest.ResultRest;
import com.svceindore.examservice.repositories.ExamRepository;
import com.svceindore.examservice.repositories.PaperRepository;
import com.svceindore.examservice.repositories.ResultRepository;
import com.svceindore.examservice.repositories.SolutionRepository;
import net.minidev.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.*;

/**
 * Date: 20/03/21
 * Time: 5:36 PM
 **/
@RequestMapping("/results")
@RestController
public class ResultControllers {
    private final ExamRepository examRepository;
    private final PaperRepository paperRepository;
    private final SolutionRepository solutionRepository;
    private final ResultRepository resultRepository;
    private final MongoTemplate mongoTemplate;

    public ResultControllers(ExamRepository examRepository, PaperRepository paperRepository, SolutionRepository solutionRepository, ResultRepository resultRepository, MongoTemplate mongoTemplate) {
        this.examRepository = examRepository;
        this.paperRepository = paperRepository;
        this.solutionRepository = solutionRepository;
        this.resultRepository = resultRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @PostMapping("/declareOfflineResult/{examId}")
    public ResponseEntity<?> uploadMark(@PathVariable String examId, @RequestBody List<Result> results) {
        System.out.println(results);
        JSONObject res = new JSONObject();

        Optional<ExamDetail> optional = examRepository.findById(examId);
        if (optional.isPresent()) {
            ExamDetail examDetail = optional.get();

            if (examDetail.getScheduledOn().getTime() + examDetail.getDuration() * 60000L < new Date().getTime()) {
                examDetail.setResultDeclared(true);
                if (!examDetail.isOnlineMode()) {

                    results.forEach(result -> {
                        result.setExamId(examId);
                    });

                    //First delete the result of student with same exam id in case of result is reevaluated
                    resultRepository.deleteByExamId(examId);
                    resultRepository.insert(results);

                    examRepository.save(examDetail);
                    res.appendField("status", true);
                    res.appendField("message", "Result declared successfully.");
                }
            } else {
                res.appendField("status", false);
                res.appendField("message", "Exam not ended yet. Result can only be declared after exam is over.");
            }

        } else {
            res.appendField("status", false);
            res.appendField("message", "Exam detail not found.");
        }

        return ResponseEntity.ok(res.toString());
    }

    @RolesAllowed(Roles.ROLE_FACULTY)
    @PostMapping("/declare")
    public ResponseEntity<?> declareResult(@RequestParam String examId) {
        JSONObject res = new JSONObject();

        Optional<ExamDetail> optional = examRepository.findById(examId);
        if (optional.isPresent()) {
            ExamDetail examDetail = optional.get();

            if (examDetail.getScheduledOn().getTime() + examDetail.getDuration() * 60000L < new Date().getTime()) {
                examDetail.setResultDeclared(true);
                if (examDetail.isOnlineMode()) {
                    Optional<Paper> optionalPaper = paperRepository.findById(examId);
                    if (optionalPaper.isPresent() && (optionalPaper.get().getAnswers() != null)) {
                        Paper paper = optionalPaper.get();
                        Map<Integer, Answer> map = new HashMap<>();
                        for (Answer answer : paper.getAnswers()) {
                            map.put(answer.getId(), answer);
                        }
                        Map<Integer, Question> questionMap = new HashMap<>();
                        for (Question question : paper.getQuestions()) {
                            questionMap.put(question.getId(), question);
                        }

                        List<Solution> solutions = solutionRepository.findByPaperId(examId);
                        List<Result> results = new ArrayList<>();

                        for (Solution solution : solutions) {
                            List<Answer> answers = solution.getAnswers();
                            int markObtained = 0;
                            for (Answer answer : answers) {
                                if (answer.getValue().equals(map.get(answer.getId()).getValue())) {
                                    markObtained += questionMap.get(answer.getId()).getMark();
                                }
                            }

                            results.add(new Result(
                                    null,
                                    examId,
                                    solution.getStudentId(),
                                    true,
                                    markObtained
                            ));
                        }
                        //First delete the result of student with same exam id in case of result is reevaluated
                        resultRepository.deleteByExamId(examId);
                        resultRepository.insert(results);

                        examRepository.save(examDetail);
                        res.appendField("status", true);
                        res.appendField("message", "Result declared successfully.");

                    } else {
                        res.appendField("status", false);
                        res.appendField("message", "Paper solution not found.");
                    }
                }
            } else {
                res.appendField("status", false);
                res.appendField("message", "Exam not ended yet. Result can only be declared after exam is over.");
            }

        } else {
            res.appendField("status", false);
            res.appendField("message", "Exam detail not found.");
        }

        return ResponseEntity.ok(res.toString());
    }

    @GetMapping("/all")
    public List<ResultRest> getResults(@RequestParam(required = false, defaultValue = "") String courseId,
                                       @RequestParam(required = false, defaultValue = "") String branchId,
                                       @RequestParam(required = false, defaultValue = "") String sessionId,
                                       @RequestParam(required = false, defaultValue = "") String subjectId,
                                       @RequestParam(required = false, defaultValue = "-1") int semesterId
            , Principal principal) {

        List<ResultRest> results = new ArrayList<>();
        boolean isAdmin = false;

        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(Roles.ROLE_FACULTY)) {
                isAdmin = true;
                break;
            }
        }
        Query query = new Query();
        Criteria criteria = Criteria.where("courseId").is(courseId)
                .and("branchId").is(branchId)
                .and("sessionId").is(sessionId)
                .and("semester").is(semesterId);

        if (!subjectId.isEmpty()) criteria.and("subjectId").is(subjectId);

        query.addCriteria(criteria);
        List<ExamDetail> examDetails = mongoTemplate.find(query, ExamDetail.class);
        String username = principal.getName();
        for (ExamDetail detail : examDetails) {
            Query qr = new Query();
            Criteria cr = Criteria.where("examId").is(detail.getId());
            if (!isAdmin) {
                cr.and("studentId").is(username);
            }
            qr.addCriteria(cr);

            mongoTemplate.find(qr, ResultRest.class, "result").forEach(result -> {
                result.setExamTitle(detail.getTitle());
                result.setExamSubject(detail.getSubjectId());
                results.add(result);
            });
        }
        return results;
    }
}
