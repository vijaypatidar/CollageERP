package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.model.course.Course;
import com.svceindore.uiservice.model.course.Enrolled;
import com.svceindore.uiservice.model.course.Semester;
import com.svceindore.uiservice.model.course.Session;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Vijay Patidar
 * Date: 10/03/21
 * Time: 10:07 PM
 **/
@Controller
@RequestMapping("/exams")
public class ExamController {

    private final KeycloakRestTemplate restTemplate;

    public ExamController(KeycloakRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/create-new-exam.html")
    public String getCreateExamPage(Model model) {
        ResponseEntity<Course[]> entity = restTemplate.getForEntity("lb://course-service/api/course/getCourseList", Course[].class);
        ResponseEntity<Session[]> entity2 = restTemplate.getForEntity("lb://course-service/api/course/session", Session[].class);
        ResponseEntity<Semester[]> entity3 = restTemplate.getForEntity("lb://course-service/api/course/semesters", Semester[].class);
        model.addAttribute("courses", entity.getBody());
        model.addAttribute("sessions", entity2.getBody());
        model.addAttribute("semesters", entity3.getBody());

        return "create-exam-detail";
    }
}
