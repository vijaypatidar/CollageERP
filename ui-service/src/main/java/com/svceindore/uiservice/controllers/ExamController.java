package com.svceindore.uiservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svceindore.uiservice.clients.CourseClient;
import com.svceindore.uiservice.configs.Roles;
import com.svceindore.uiservice.model.course.*;
import com.svceindore.uiservice.model.exam.ExamDetail;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.util.Date;

/**
 * Created by Vijay Patidar
 * Date: 10/03/21
 * Time: 10:07 PM
 **/
@Controller
@RequestMapping("/exams")
public class ExamController {

    private final KeycloakRestTemplate restTemplate;
    private final CourseClient courseClient;

    public ExamController(KeycloakRestTemplate restTemplate, CourseClient courseClient) {
        this.restTemplate = restTemplate;
        this.courseClient = courseClient;
    }

    @GetMapping("/create-new-exam.html")
    public String getCreateExamPage(Model model) {
        model.addAttribute("courses", courseClient.getCourses());
        model.addAttribute("sessions", courseClient.getSessions());
        model.addAttribute("semesters", courseClient.getSemesters());
        return "create-exam-detail";
    }

    @GetMapping("/edit-exam-detail.html")
    public String getEditExamPage(Model model, @RequestParam String examDetailId) {
        ExamDetail examDetail = restTemplate.getForEntity("lb://exam-service/api/exam/exam/"+examDetailId,ExamDetail.class).getBody();
        model.addAttribute("examDetail",examDetail);
        model.addAttribute("courses", courseClient.getCourses());
        model.addAttribute("subjects", courseClient.getSubjects(examDetail.getCourseId()));
        model.addAttribute("branches", courseClient.getBranches(examDetail.getCourseId()));
        model.addAttribute("sessions", courseClient.getSessions());
        model.addAttribute("semesters", courseClient.getSemesters());

        return "edit-exam-detail";
    }

    @GetMapping("/view-all-exams.html")
    public String getViewAllExamsPage(Model model, @RequestParam(required = false, defaultValue = "") String courseId,
                                      @RequestParam(required = false, defaultValue = "") String branchId,
                                      @RequestParam(required = false, defaultValue = "") String sessionId) {

        ResponseEntity<ExamDetail[]> entity3 = restTemplate.getForEntity("lb://exam-service/api/exam/exams?courseId=" + courseId + "&branchId=" + branchId + "&sessionId=" + sessionId, ExamDetail[].class);
        model.addAttribute("courseId", courseId);
        model.addAttribute("branchId", branchId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("courses", courseClient.getCourses());
        model.addAttribute("sessions", courseClient.getSessions());
        model.addAttribute("exams", entity3.getBody());


        if (!courseId.isEmpty()) {
            model.addAttribute("branches", courseClient.getBranches(courseId));
        }

        return "view-all-exams";
    }

    @GetMapping("/view-exam-for-enrolled-courses.html")
    public String getViewExamsForEnrolledCoursePage(Model model) {

        ResponseEntity<ExamDetail[]> entity3 = restTemplate.getForEntity("lb://exam-service/api/exam/my-exams", ExamDetail[].class);
        model.addAttribute("courses", courseClient.getCourses());
        model.addAttribute("exams", entity3.getBody());
        model.addAttribute("semesters",courseClient.getSemesters());
        model.addAttribute("semester",5);
        return "view-exam-for-enrolled-courses";
    }

    @RolesAllowed(Roles.ADMIN_ROLE)
    @GetMapping("/create-new-paper.html")
    public String createPaperPage(Model model,@RequestParam String paperId){
        model.addAttribute("paperId",paperId);
        return "design-new-paper";
    }
}
