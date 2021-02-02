package com.svceindore.uiservice.controllers;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin("*")
@RequestMapping("/")
@Controller
public class WebController {

    private final DiscoveryClient discoveryClient;

    public WebController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
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
