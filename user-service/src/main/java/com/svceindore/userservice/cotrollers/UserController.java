package com.svceindore.userservice.cotrollers;

import com.svceindore.userservice.client.KeycloakClient;
import com.svceindore.userservice.configs.Roles;
import com.svceindore.userservice.model.Student;
import com.svceindore.userservice.model.User;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Set;
import java.util.logging.Logger;

@RequestMapping("api/v1/user/")
@RestController
public class UserController {

    private final KeycloakClient keycloakClient;
    Logger logger = Logger.getLogger(getClass().getSimpleName());

    public UserController(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    @RolesAllowed({Roles.ADMIN_ROLE})
    @PostMapping("/createStudent")
    public ResponseEntity<String> getCreateStudent(@RequestBody Student student) {
        logger.info("Create Student " + student.toString());
        Response re = keycloakClient.createUser(student);
        if (re.getStatus() == 201) {
            return ResponseEntity.status(HttpStatus.CREATED).body("user account created successfully");
        } else if (re.getStatus() == 409) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("user already exist for this username or email.");
        }
        return null;
    }

    @GetMapping("/myProfile")
    public User getMyProfile(Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
        User user = new User();
        user.setUsername(accessToken.getPreferredUsername());
        user.setEmail(accessToken.getEmail());
        user.setFirstName(accessToken.getGivenName());
        user.setLastName(accessToken.getFamilyName());
        return user;
    }

    @GetMapping("/myRoles")
    public Set<String> getMyRoles(Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        return keycloakAuthenticationToken.getAccount().getRoles();
    }
}
