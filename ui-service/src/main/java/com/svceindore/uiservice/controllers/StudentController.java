package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.configs.Roles;
import com.svceindore.uiservice.utils.DataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/student")
@Controller
public class StudentController {

    private final RestTemplate restTemplate;
    private final DataUtils dataUtils;

    public StudentController(RestTemplate restTemplate, DataUtils dataUtils) {
        this.restTemplate = restTemplate;
        this.dataUtils = dataUtils;
    }

    @RequestMapping({"/student-home.html", "/student-home"})
    public String getHome() {
        return "student-home";
    }

    @RolesAllowed({Roles.ADMIN_ROLE})
    @RequestMapping({"/create-student-account.html", "/create-student-account"})
    public String createStudentAccount(HttpServletRequest request) {
        request.setAttribute("states",dataUtils.getStates());
        request.setAttribute("countries",dataUtils.getCountries());
        return "create-student-account";
    }

    @ResponseBody
    @RequestMapping("/role")
    public String getRoles() {
        return restTemplate.getForObject("http://user-service/api/user/myRoles", String.class);
    }



}
