package com.svceindore.uiservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/student")
@Controller
public class StudentController {

    private final RestTemplate restTemplate;

    public StudentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping({"/student-home.html", "/student-home"})
    public String getHome() {
        return "student-home";
    }

    @RequestMapping({"/create-student-account.html", "/create-student-account"})
    public String createStudentAccount() {
        return "create-student-account";
    }

    @ResponseBody
    @RequestMapping("/role")
    public String getRoles() {
        return restTemplate.getForObject("http://user-service/api/user/myRoles", String.class);
    }



}
