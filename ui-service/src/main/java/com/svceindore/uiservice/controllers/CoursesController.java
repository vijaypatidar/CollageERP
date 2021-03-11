package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.model.course.*;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Logger;

/**
 * Created by Vijay Patidar
 * Date: 05/03/21
 * Time: 9:28 AM
 **/
@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final Logger logger = Logger.getLogger(getClass().getSimpleName());
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

    @GetMapping("/update-course-detail.html")
    public String getUpdateCoursePage(@RequestParam String courseId, Model model) {
        ResponseEntity<Course> entity = restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + courseId, Course.class);
        model.addAttribute("course", entity.getBody());
        return "update-course-detail";
    }

    @GetMapping("/add-branch.html")
    public String getAddBranchPage(@RequestParam String courseId, Model model) {
        ResponseEntity<Course> entity = restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + courseId, Course.class);
        if (entity.getStatusCode().value() == 200) {
            model.addAttribute("courseId", courseId);
            model.addAttribute("courseName", entity.getBody().getName());
        }
        return "add-branch";
    }

    @GetMapping("/update-branch.html")
    public String getUpdateBranchPage(@RequestParam String branchId, Model model) {
        Branch branch = restTemplate.getForEntity("lb://course-service/api/course/branch/"+branchId,Branch.class).getBody();
        Course course = restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + branch.getCourseId(), Course.class).getBody();
        model.addAttribute("courseId", branch.getCourseId());
        model.addAttribute("courseName", course.getName());
        model.addAttribute("branch",branch);
        return "update-branch";
    }

    @GetMapping("/manage-branches-for-course.html")
    public String getManageBranchForCoursePage(@RequestParam String courseId, Model model) {
        ResponseEntity<Course> entity = restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + courseId, Course.class);
        if (entity.getStatusCode().value() == 200) {
            ResponseEntity<Branch[]> entity1 = restTemplate.getForEntity("lb://course-service/api/course/getBranchList/" + courseId, Branch[].class);
            if (entity1.getStatusCode().value() == 200) {
                model.addAttribute("courseId", courseId);
                model.addAttribute("courseName", entity.getBody().getName());
                model.addAttribute("branches", entity1.getBody());
            }
        }
        return "manage-branches-for-course";
    }

    @GetMapping("manage-subject.html")
    public String manageSubjectForCourse(@RequestParam String courseId, Model model) {
        ResponseEntity<Subject[]> entity = restTemplate.getForEntity("lb://course-service/api/course/subject?courseId=" + courseId, Subject[].class);
        if (entity.getStatusCode().value() == 200) {
            model.addAttribute("courseId", courseId);
            model.addAttribute("subjects", entity.getBody());
        }
        return "manage-subject";
    }

    @GetMapping("add-subject-in-course.html")
    public String addSubjectInCourse(@RequestParam String courseId, Model model) {
        logger.info("Add subject courseId = "+courseId);
        ResponseEntity<Course> entity = restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + courseId, Course.class);
        if (entity.getStatusCode().value() == 200) {
            model.addAttribute("course", entity.getBody());
        }
        return "add-subject-in-course";
    }

    @GetMapping("enroll-student-in-course.html")
    public String enrollStudent(Model model, @RequestParam(required = false, defaultValue = "") String studentUsername) {
        ResponseEntity<Course[]> entity = restTemplate.getForEntity("lb://course-service/api/course/getCourseList", Course[].class);
        ResponseEntity<Session[]> entity1 = restTemplate.getForEntity("lb://course-service/api/course/session", Session[].class);
        model.addAttribute("studentUsername", studentUsername);
        model.addAttribute("courses", entity.getBody());
        model.addAttribute("sessions", entity1.getBody());
        return "enroll-student-in-course";
    }

    @GetMapping("enrolled-student-detail.html")
    public String enrolledStudentsDetail(Model model, @RequestParam(required = false, defaultValue = "") String courseId,
                                         @RequestParam(required = false, defaultValue = "") String branchId,
                                         @RequestParam(required = false, defaultValue = "") String sessionId) {
        System.out.println(courseId + " " + branchId);
        ResponseEntity<Course[]> entity = restTemplate.getForEntity("lb://course-service/api/course/getCourseList", Course[].class);
        ResponseEntity<Enrolled[]> entity1 = restTemplate.getForEntity("lb://course-service/api/course/enrolledStudents?courseId=" + courseId + "&branchId=" + branchId + "&sessionId=" + sessionId, Enrolled[].class);
        ResponseEntity<Session[]> entity2 = restTemplate.getForEntity("lb://course-service/api/course/session", Session[].class);
        model.addAttribute("courseId", courseId);
        model.addAttribute("branchId", branchId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("courses", entity.getBody());
        model.addAttribute("enrolls", entity1.getBody());
        model.addAttribute("sessions", entity2.getBody());


        if (!courseId.isEmpty()) {
            model.addAttribute("branches", restTemplate.getForEntity("lb://course-service/api/course/getBranchList/" + courseId, Branch[].class).getBody());
        }

        return "enrolled-student-detail";
    }

    @GetMapping("/manage-session.html")
    public String getManageSessionPage(Model model) {
        ResponseEntity<Session[]> entity = restTemplate.getForEntity("lb://course-service/api/course/session", Session[].class);
        if (entity.getStatusCode().value() == 200) {
            model.addAttribute("sessions", entity.getBody());
        }
        return "manage-session";
    }

    @GetMapping("/add-session.html")
    public String getAddSessionPage() {
        return "add-new-session";
    }

}
