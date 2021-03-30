package com.svceindore.uiservice.clients;

import com.svceindore.uiservice.model.course.*;
import com.svceindore.uiservice.model.user.User;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.stereotype.Component;

/**
 * Date: 30/03/21
 * Time: 10:24 PM
 **/
@Component
public class UserClient {
    private final KeycloakRestTemplate restTemplate;

    public UserClient(KeycloakRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUser(String username) {
        return restTemplate.getForEntity("lb://user-service/api/user/profile/" + username, User.class).getBody();
    }

    public User getCurrentUser() {
        return restTemplate.getForEntity("lb://user-service/api/user/profile", User.class).getBody();
    }
}
