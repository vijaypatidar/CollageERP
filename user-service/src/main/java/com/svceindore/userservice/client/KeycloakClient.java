package com.svceindore.userservice.client;

import com.svceindore.userservice.configs.Roles;
import com.svceindore.userservice.model.Student;
import com.svceindore.userservice.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Component
public class KeycloakClient {

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
            userRepresentation.setRealmRoles(getStudentRoles());
        }

        userRepresentation.setAttributes(Collections.singletonMap("origin", Collections.singletonList("keycloak admin client")));

        //setting password
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(user.getPassword());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        try {
            Response response = keycloak.realm(realm).users().create(userRepresentation);
            logger.info("Response: " + response.getStatus());
            logger.info("Response: " + response.getMetadata());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getStudentRoles() {
        ArrayList<String> studentRoles = new ArrayList<>();
        studentRoles.add(Roles.USER_ROLE);
        return studentRoles;
    }

}
