package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.clients.CourseClient;
import com.svceindore.uiservice.model.course.*;
import com.svceindore.uiservice.model.exam.ExamDetail;
import com.svceindore.uiservice.model.timetables.Time;
import com.svceindore.uiservice.model.timetables.TimeTable;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final CourseClient courseClient;

    public CoursesController(KeycloakRestTemplate restTemplate, CourseClient courseClient) {
        this.restTemplate = restTemplate;
        this.courseClient = courseClient;
    }

    @GetMapping("/manage-courses.html")
    public String getManageCoursePage(Model model) {
        model.addAttribute("courses", courseClient.getCourses());
        return "manage-courses";
    }

    @GetMapping("/add-course.html")
    public String getAddCoursePage() {
        return "add-course";
    }

    @GetMapping("/update-course-detail.html")
    public String getUpdateCoursePage(@RequestParam String courseId, Model model) {
        model.addAttribute("course", courseClient.getCourse(courseId));
        return "update-course-detail";
    }

    @GetMapping("/add-branch.html")
    public String getAddBranchPage(@RequestParam String courseId, Model model) {
        Course course = courseClient.getCourse(courseId);
        if (course != null) {
            model.addAttribute("courseId", courseId);
            model.addAttribute("courseName", course.getName());
        }
        return "add-branch";
    }

    @GetMapping("/update-branch.html")
    public String getUpdateBranchPage(@RequestParam String branchId, Model model) {
        Branch branch = courseClient.getBranch(branchId);
        Course course = courseClient.getCourse(branch.getCourseId());
        model.addAttribute("courseId", branch.getCourseId());
        model.addAttribute("courseName", course.getName());
        model.addAttribute("branch", branch);
        return "update-branch";
    }

    @GetMapping("/manage-branches-for-course.html")
    public String getManageBranchForCoursePage(@RequestParam String courseId, Model model) {

        Course course = courseClient.getCourse(courseId);
        Branch[] branches = courseClient.getBranches(courseId);
        model.addAttribute("courseId", courseId);
        model.addAttribute("courseName", course.getName());
        model.addAttribute("branches", branches);

        return "manage-branches-for-course";
    }

    @GetMapping("manage-subject.html")
    public String manageSubjectForCourse(@RequestParam String courseId, Model model) {
        model.addAttribute("courseId", courseId);
        model.addAttribute("subjects", courseClient.getSubjects(courseId));
        return "manage-subject";
    }

    @GetMapping("add-subject-in-course.html")
    public String addSubjectInCourse(@RequestParam String courseId, Model model) {
        logger.info("Add subject courseId = " + courseId);
        Course course = courseClient.getCourse(courseId);
        if (course != null) {
            model.addAttribute("course", course);
        }
        return "add-subject-in-course";
    }

    @GetMapping("enroll-student-in-course.html")
    public String enrollStudent(Model model, @RequestParam(required = false, defaultValue = "") String studentUsername) {
        model.addAttribute("studentUsername", studentUsername);
        model.addAttribute("courses", courseClient.getCourses());
        model.addAttribute("sessions", courseClient.getSessions());
        return "enroll-student-in-course";
    }

    @GetMapping("enrolled-student-detail.html")
    public String enrolledStudentsDetail(Model model, @RequestParam(required = false, defaultValue = "") String courseId,
                                         @RequestParam(required = false, defaultValue = "") String branchId,
                                         @RequestParam(required = false, defaultValue = "") String sessionId) {
        ResponseEntity<Enrolled[]> entity1 = restTemplate.getForEntity("lb://course-service/api/course/enrolledStudents?courseId=" + courseId + "&branchId=" + branchId + "&sessionId=" + sessionId, Enrolled[].class);
        model.addAttribute("courseId", courseId);
        model.addAttribute("branchId", branchId);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("courses", courseClient.getCourses());
        model.addAttribute("enrolls", entity1.getBody());
        model.addAttribute("sessions", courseClient.getSessions());


        if (!courseId.isEmpty()) {
            model.addAttribute("branches", courseClient.getBranches(courseId));
        }

        return "enrolled-student-detail";
    }

    @GetMapping("my-enrolled-courses-detail.html")
    public String getMyEnrolledCoursesPage(Model model) {
        Enrolled[] enrolleds = restTemplate.getForEntity("lb://course-service/api/course/self-enrolls", Enrolled[].class).getBody();

        for (Enrolled enrolled : enrolleds) {
            enrolled.setCourseName(
                    courseClient.getCourse(enrolled.getCourseId()).getName()
            );
            enrolled.setBranchName(
                    courseClient.getBranch(enrolled.getBranchId()).getName()
            );
            enrolled.setSessionName(
                    courseClient.getSession(enrolled.getSessionId()).getName()
            );
        }
        model.addAttribute("courses", courseClient.getCourses());
        model.addAttribute("enrolls", enrolleds);
        model.addAttribute("sessions", courseClient.getSessions());

        return "my-enrolled-courses-detail";
    }

    @GetMapping("/manage-session.html")
    public String getManageSessionPage(Model model) {
        model.addAttribute("sessions", courseClient.getSessions());
        return "manage-session";
    }

    @GetMapping("/add-session.html")
    public String getAddSessionPage() {
        return "add-new-session";
    }

    @GetMapping("/set-time-table.html")
    public String getSetTimeTablePage(Model model) {
        model.addAttribute("sessions", courseClient.getSessions());
        model.addAttribute("courses", courseClient.getCourses());
        return "set-time-table";
    }

    @GetMapping("/view-time-table.html")
    public String getViewTimeTablePage(Model model, @RequestParam(required = false, defaultValue = "") String courseId,
                                       @RequestParam(required = false, defaultValue = "") String branchId,
                                       @RequestParam(required = false, defaultValue = "") String sessionId) {
        model.addAttribute("sessions", courseClient.getSessions());
        model.addAttribute("courses", courseClient.getCourses());

        if(courseId.isEmpty()||branchId.isEmpty()||sessionId.isEmpty()){

        }else {
            model.addAttribute("courseId",courseId);
            model.addAttribute("branchId",branchId);
            model.addAttribute("sessionId",sessionId);
            model.addAttribute("branches",courseClient.getBranches(courseId));

            try{

                ResponseEntity<TimeTable> entity = restTemplate.getForEntity("lb://course-service//api/course/timeTable?courseId="+courseId+"&branchId="+branchId+"&sessionId=" + sessionId , TimeTable.class);
                TimeTable timeTable = entity.getBody();
                if (entity.getStatusCodeValue()==200&&timeTable!=null){
                    List<List<String>> lectures = timeTable.getLectures();
                    Subject[] subjects = courseClient.getSubjects(courseId);
                    Map<String,String> map = new HashMap<>();
                    for (Subject subject:subjects){
                        map.put(subject.getId(),subject.getId()+"("+subject.getName()+")");
                    }
                    String[][] time = new String[timeTable.getLectures().get(0).size()][7];
                    for (int d=0;d<6;d++){
                        List<String> lecture = lectures.get(d);
                        for (int i=0;i<time.length;i++){
                            time[i][d+1]=map.get(lecture.get(i));
                        }
                    }

                    List<Time> times = timeTable.getTime();
                    for (int i=0;i<time.length;i++) {
                        Time t = times.get(i);
                        time[i][0] = t.inAmPm(t.getStart())+"-"+t.inAmPm(t.getEnd());
                    }

                    model.addAttribute("timeTable",time);
                }
            }catch (Exception e){

                e.printStackTrace();
            }

        }
        return "view-time-table";
    }
}
