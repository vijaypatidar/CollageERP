package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.model.course.Branch;
import com.svceindore.uiservice.model.course.Course;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Vijay Patidar
 * Date: 05/03/21
 * Time: 9:28 AM
 **/
@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final KeycloakRestTemplate restTemplate;

    public CoursesController(KeycloakRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/manage-courses.html")
    public String getManageCoursePage(Model model) {
        ResponseEntity<Course[]> entity = restTemplate.getForEntity("lb://course-service/api/course/getCourseList", Course[].class);
        if (entity.getStatusCode().value() == 200) {
            model.addAttribute("courses", entity.getBody());
        }
        return "manage-courses";
    }

    @GetMapping("/add-course.html")
    public String getAddCoursePage() {
        return "add-course";
    }

    @GetMapping("/add-branch.html")
    public String getAddBranchPage(@RequestParam String courseId,Model model) {
        ResponseEntity<Course> entity = restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + courseId, Course.class);
        if (entity.getStatusCode().value()==200){
            model.addAttribute("courseId",courseId);
            model.addAttribute("courseName",entity.getBody().getName());
        }
        return "add-branch";
    }

    @GetMapping("/manage-branches-for-course.html")
    public String getManageBranchForCoursePage(@RequestParam String courseId,Model model) {
        ResponseEntity<Course> entity = restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + courseId, Course.class);
        if (entity.getStatusCode().value()==200){
            ResponseEntity<Branch[]> entity1 = restTemplate.getForEntity("lb://course-service/api/course/getBranchList/" + courseId, Branch[].class);
            if (entity1.getStatusCode().value()==200){
                model.addAttribute("courseId",courseId);
                model.addAttribute("courseName",entity.getBody().getName());
                model.addAttribute("branches",entity1.getBody());
            }
        }
        return "manage-branches-for-course";
    }

    @GetMapping("enroll-student-in-course.html")
    public String enrollStudent(Model model,@RequestParam String studentUsername){
        ResponseEntity<Course[]> entity = restTemplate.getForEntity("lb://course-service/api/course/getCourseList", Course[].class);
        model.addAttribute("studentUsername",studentUsername);
        model.addAttribute("courses",entity.getBody());
        return "enroll-student-in-course";
    }

}
