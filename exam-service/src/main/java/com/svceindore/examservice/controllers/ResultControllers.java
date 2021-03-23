package com.svceindore.examservice.controllers;

import com.svceindore.examservice.configs.Roles;
import com.svceindore.examservice.models.*;
import com.svceindore.examservice.repositories.ExamRepository;
import com.svceindore.examservice.repositories.PaperRepository;
import com.svceindore.examservice.repositories.ResultRepository;
import com.svceindore.examservice.repositories.SolutionRepository;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
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

    public ResultControllers(ExamRepository examRepository, PaperRepository paperRepository, SolutionRepository solutionRepository, ResultRepository resultRepository) {
        this.examRepository = examRepository;
        this.paperRepository = paperRepository;
        this.solutionRepository = solutionRepository;
        this.resultRepository = resultRepository;
    }

    @RolesAllowed(Roles.ROLE_FACULTY)
    @PostMapping("/declare")
    public ResponseEntity<?> declareResult(@RequestParam String examId) {
        JSONObject res = new JSONObject();

        Optional<ExamDetail> optional = examRepository.findById(examId);
        if (optional.isPresent()) {
            ExamDetail examDetail = optional.get();

            if (examDetail.getScheduledOn().getTime()+examDetail.getDuration()*60000L < new Date().getTime()) {
                examDetail.setResultDeclared(true);
                if (examDetail.isOnlineMode()) {
                    Optional<Paper> optionalPaper = paperRepository.findById(examId);
                    if (optionalPaper.isPresent()&&(optionalPaper.get().getAnswers()!=null)){
                        Paper paper = optionalPaper.get();
                        Map<Integer, Answer> map = new HashMap<>();
                        for (Answer answer: paper.getAnswers()){
                            map.put(answer.getId(),answer);
                        }
                        Map<Integer, Question> questionMap = new HashMap<>();
                        for (Question question: paper.getQuestions()){
                            questionMap.put(question.getId(),question);
                        }

                        List<Solution> solutions = solutionRepository.findByPaperId(examId);
                        List<Result> results = new ArrayList<>();

                        for (Solution solution:solutions){
                            List<Answer> answers = solution.getAnswers();
                            int markObtained = 0;
                            for (Answer answer:answers){
                                if (answer.getValue().equals(map.get(answer.getId()).getValue())){
                                    markObtained+=questionMap.get(answer.getId()).getMark();
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

                    }else {
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

}
