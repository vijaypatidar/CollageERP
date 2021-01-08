package com.svceindore.uiservice.controllers;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class WebController {

    private final DiscoveryClient discoveryClient;

    public WebController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping({"/", "/home"})
    public String getStudentHome() {

        ServiceInstance instances = discoveryClient.getInstances("gateway-service").get(0);

        return String.format("redirect:http://%s:%d/student/home", instances.getHost(), instances.getPort());
    }

}
