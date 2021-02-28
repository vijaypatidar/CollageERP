package com.svceindore.userservice.cotrollers;

import com.svceindore.userservice.client.KeycloakClient;
import com.svceindore.userservice.configs.Roles;
import com.svceindore.userservice.model.Faculty;
import com.svceindore.userservice.model.Student;
import com.svceindore.userservice.model.User;
import net.minidev.json.JSONObject;
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

@RequestMapping("/api/user/")
@RestController
public class UserController {

    private final KeycloakClient keycloakClient;
    Logger logger = Logger.getLogger(getClass().getSimpleName());

    public UserController(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    @RolesAllowed({Roles.ADMIN_ROLE})
    @PostMapping("/createStudent")
    public ResponseEntity<String> createStudent(@RequestBody Student student) {
        logger.info("Create student account request " + student.toString());
        JSONObject response = new JSONObject();
        Response re = keycloakClient.createUser(student);
        if (re.getStatus() == 201) {
            response.appendField("status", true);
            response.appendField("message", "User account created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
        } else if (re.getStatus() == 409) {
            response.appendField("status", false);
            response.appendField("message", "User already exist for this username or email.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response.toString());
        }else if (re.getStatus()==403){
            response.appendField("status", false);
            response.appendField("message", "Internal access denied.");
            return ResponseEntity.ok(response.toString());
        }

        return null;
    }

    @RolesAllowed({Roles.ADMIN_ROLE})
    @PostMapping("/createFaculty")
    public ResponseEntity<String> createFaculty(@RequestBody Faculty faculty) {
        logger.info("Create faculty account request " + faculty.toString());
        Response re = keycloakClient.createUser(faculty);
        JSONObject response = new JSONObject();
        if (re.getStatus() == 201) {
            response.appendField("status", true);
            response.appendField("message", "User account created successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
        } else if (re.getStatus() == 409) {
            response.appendField("status", false);
            response.appendField("message", "User already exist for this username or email.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response.toString());
        }else if (re.getStatus()==403){
            response.appendField("status", false);
            response.appendField("message", "Internal access denied.");
            return ResponseEntity.ok(response.toString());
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

    @GetMapping("/detail")
    public String getApiDetail() {
        return "API for managing user.";
    }

}
