package com.svceindore.gatewayservice.controllers;/*
 *Created by vijay
 *Date: 23/01/21
 *Time: 12:55 PM
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@Controller
public class AuthController {

    @Value("${PROJECT_ROOT_URL:http://localhost:8080}")
    private String PROJECT_ROOT;


    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/auth/login"), req ->
                ServerResponse.temporaryRedirect(URI.create(PROJECT_ROOT + "/home"))
                        .build());
    }

}
