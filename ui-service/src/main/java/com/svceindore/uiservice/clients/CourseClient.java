package com.svceindore.uiservice.clients;

import com.svceindore.uiservice.model.course.*;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Vijay Patidar
 * Date: 12/03/21
 * Time: 2:24 PM
 **/
@Component
public class CourseClient {
    private final KeycloakRestTemplate restTemplate;

    public CourseClient(KeycloakRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Course[] getCourses() {
        return restTemplate.getForEntity("lb://course-service/api/course/getCourseList", Course[].class).getBody();
    }
    public Course getCourse(String courseId) {
        return restTemplate.getForEntity("lb://course-service/api/course/courseInfo/" + courseId, Course.class).getBody();
    }

    public Session[] getSessions() {
        return restTemplate.getForEntity("lb://course-service/api/course/session", Session[].class).getBody();
    }

    public Branch getBranch(String branchId) {
        return restTemplate.getForEntity("lb://course-service/api/course/branch/" + branchId, Branch.class).getBody();
    }

    public Branch[] getBranches(String courseId){
        return restTemplate.getForEntity("lb://course-service/api/course/getBranchList/" + courseId, Branch[].class).getBody();
    }

    public Subject[] getSubjects(String courseId){
        return restTemplate.getForEntity("lb://course-service/api/course/subject?courseId=" + courseId, Subject[].class).getBody();
    }

    public Semester[] getSemesters() {
        return restTemplate.getForEntity("lb://course-service/api/course/semesters", Semester[].class).getBody();
    }
}
