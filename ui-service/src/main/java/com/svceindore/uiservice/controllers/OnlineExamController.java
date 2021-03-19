package com.svceindore.uiservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svceindore.uiservice.configs.Roles;
import com.svceindore.uiservice.model.exam.ExamDetail;
import com.svceindore.uiservice.model.exam.Paper;
import com.svceindore.uiservice.model.exam.Question;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 25/02/21
 * Time: 2:24 PM
 **/
@Controller
@RequestMapping("/exam/online")
public class OnlineExamController {
    private final KeycloakRestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public OnlineExamController(KeycloakRestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @RequestMapping("/start-exam")
    public String getOnlinePaper(Model model, @RequestParam String examId) throws JsonProcessingException {

        ExamDetail examDetail = restTemplate.getForEntity("lb://exam-service/api/exam/exam/" + examId, ExamDetail.class).getBody();

        if (examDetail == null) {
            return "505";
        }

        long sTime = examDetail.getScheduledOn().getTime();
        long eTime = sTime + examDetail.getDuration() * 60000L;
        long cTime = new Date().getTime();

        if (sTime <= cTime && cTime <= eTime) {
            ResponseEntity<String> entity = restTemplate.getForEntity(
                    "lb://exam-service/api/exam/papers/paper/" + examId,
                    String.class
            );
            if (entity.getStatusCode() == HttpStatus.OK) {
                System.out.println(entity.getBody());

                Paper p = objectMapper.readValue(entity.getBody(), Paper.class);

                model.addAttribute("questions", p.getQuestions())
                        .addAttribute("paperTitle", examDetail.getTitle())
                        .addAttribute("time", examDetail.getDuration() + " Min")
                        .addAttribute("endTimeMillisecond", eTime)
                        .addAttribute("paperId", p.getId());
                return "online-exam-question-paper";
            } else {
                return "505";
            }
        } else if (eTime < cTime) {
            model.addAttribute("message", "Exam already ended.");
            return "505";
        } else {
            model.addAttribute("message", "Exam not started.");
            return "505";
        }
    }

    @RolesAllowed(Roles.ROLE_FACULTY)
    @RequestMapping("/submit-exam-solution")
    public String getSubmitOnlinePaperSolutionPage(Model model, @RequestParam String examId) throws JsonProcessingException {

        ExamDetail examDetail = restTemplate.getForEntity("lb://exam-service/api/exam/exam/" + examId, ExamDetail.class).getBody();

        if (examDetail == null) {
            return "505";
        }


        ResponseEntity<String> entity = restTemplate.getForEntity(
                "lb://exam-service/api/exam/papers/paper/" + examId,
                String.class
        );
        if (entity.getStatusCode() == HttpStatus.OK) {
            System.out.println(entity.getBody());

            Paper p = objectMapper.readValue(entity.getBody(), Paper.class);

            model.addAttribute("questions", p.getQuestions())
                    .addAttribute("paperTitle", examDetail.getTitle())
                    .addAttribute("time", examDetail.getDuration() + " Min")
                    .addAttribute("paperId", p.getId());
            return "online-exam-solution";
        } else {
            return "505";
        }

    }
}
