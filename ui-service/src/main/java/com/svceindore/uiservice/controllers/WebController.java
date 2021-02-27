package com.svceindore.uiservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin("*")
@RequestMapping("/")
@Controller
public class WebController {

    public WebController() {
    }

    @RequestMapping({"/home.html", "/home"})
    public String getStudentHome() {
        return "home";
    }

    @RequestMapping({"", "/"})
    public String getProjectHome() {
        return "project-detail";
    }



}
