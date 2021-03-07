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
import org.springframework.web.server.WebSession;
import reactor.netty.http.server.HttpServerRequest;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@Controller
public class AuthController {

    @Value("${PROJECT_HOST_ADDRESS}:8080")
    private String PROJECT_ROOT;

    @Value("${PROJECT_HOST_ADDRESS}:8180")
    private String PROJECT_AUTH_SERVER_ROOT;

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/auth/login"), req ->
                ServerResponse.temporaryRedirect(URI.create(PROJECT_ROOT + "/home"))
                        .build());
    }
    @Bean
    RouterFunction<ServerResponse> routerFunctionLogout() {
        return route(GET("/auth/logout"), req -> ServerResponse.temporaryRedirect(URI.create(PROJECT_AUTH_SERVER_ROOT + "/auth/realms/CollageERP/protocol/openid-connect/logout?redirect_uri="+PROJECT_ROOT)).build());
    }

}
