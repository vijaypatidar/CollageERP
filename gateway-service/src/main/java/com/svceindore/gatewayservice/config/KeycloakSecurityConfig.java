package com.svceindore.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.stereotype.Component;

/*
 *Created by vijay
 *Date: 22/01/21
 *Time: 11:35 PM
 */
@Component
public class KeycloakSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            ReactiveClientRegistrationRepository clientRegistrationRepository
    ) {
        http.oauth2Login();
        http.authorizeExchange()
                .pathMatchers(
                        "/",
                        "/home",
                        "/js/**",
                        "/bootstrap/**",
                        "/img/**",
                        "/css/**"
                        ).permitAll()
                .anyExchange().authenticated();
        http.csrf().disable();
        return http.build();
    }
}
