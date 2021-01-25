package com.svceindore.uiservice.controllers;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("*")
@RequestMapping("/")
@Controller
public class WebController {

    private final DiscoveryClient discoveryClient;

    public WebController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping({"/home.html", "/home"})
    public String getStudentHome(HttpServletRequest request) {
        try {
            KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
            AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
            request.setAttribute("name", accessToken.getGivenName() + " " + accessToken.getFamilyName());
        }catch (Exception ignored){

        }
        return "home";
    }

    @RequestMapping({"", "/"})
    public String getProjectHome() {
        return "project-detail";
    }



}
