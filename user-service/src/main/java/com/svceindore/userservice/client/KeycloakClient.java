package com.svceindore.userservice.client;

import com.svceindore.userservice.model.Faculty;
import com.svceindore.userservice.model.Student;
import com.svceindore.userservice.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@RefreshScope
@Component
public class KeycloakClient {

    @Value("${defaultPassword:12345}")
    private String defaultPassword;
    private final String realm;
    private final Keycloak keycloak;
    Logger logger = Logger.getLogger(getClass().getSimpleName());

    public KeycloakClient(
            @Value("${keycloak.auth-server-url:http://localhost:8180/auth}") String serverUrl,
            @Value("${keycloak.realm:CollageERP}") String realm,
            @Value("${env.MANAGE_USER_USERNAME:admin}") String username,
            @Value("${env.MANAGE_USER_PASSWORD:root}") String password
    ) {
        this.realm = realm;
        String clientId = "admin-cli";
        this.keycloak = Keycloak.getInstance(
                serverUrl,
                realm,
                username,
                password,
                clientId);
    }

    public Response createUser(User user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());


        if (user instanceof Student) {
            userRepresentation.setGroups(Collections.singletonList("Students"));
        }else if (user instanceof Faculty) {
            userRepresentation.setGroups(Collections.singletonList("faculties"));
        }

        userRepresentation.setAttributes(Collections.singletonMap("origin", Collections.singletonList("keycloak admin client")));

        //setting password
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(defaultPassword);
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        logger.info("Create account request " + user.toString() + " Password " + defaultPassword);
        try {
            Response response = keycloak.realm(realm).users().create(userRepresentation);
            logger.info("Response: " + response.getStatus());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
