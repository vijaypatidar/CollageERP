package com.svceindore.uiservice.controllers;

import com.svceindore.uiservice.configs.Roles;
import com.svceindore.uiservice.utils.DataUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;

@RequestMapping("/faculty")
@Controller
public class FacultyController {

    private final DataUtils dataUtils;

    public FacultyController(DataUtils dataUtils) {
        this.dataUtils = dataUtils;
    }

    @RolesAllowed({Roles.ROLE_ADMIN})
    @RequestMapping({"/create-faculty-account.html", "/create-faculty-account"})
    public String createStudentAccount(HttpServletRequest request) {
        request.setAttribute("states", dataUtils.getStates());
        request.setAttribute("countries", dataUtils.getCountries());
        request.setAttribute("formTitle","Faculty Registration");
        request.setAttribute("url","/api/user/createFaculty");
        return "create-user-account";
    }

}
