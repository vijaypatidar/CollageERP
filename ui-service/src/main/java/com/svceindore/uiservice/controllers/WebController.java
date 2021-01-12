package com.svceindore.uiservice.controllers;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
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
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
        AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
        request.setAttribute("name",accessToken.getGivenName()+" "+accessToken.getFamilyName());
        return "home";
    }

    @RequestMapping(path = "/logout")
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return ResponseEntity.ok("Ok,User logged out");
    }


}
