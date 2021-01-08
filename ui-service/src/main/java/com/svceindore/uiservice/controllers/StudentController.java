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

    @RequestMapping({"/home", "/home.html"})
    public String getHome() {
        return "student-home";
    }

    @ResponseBody
    @RequestMapping("/role")
    public String getRoles() {
        return restTemplate.getForObject("http://user-service/api/user/myRoles", String.class);
    }


}
