package com.svceindore.examservice.controllers;

import com.svceindore.examservice.configs.Roles;
import com.svceindore.examservice.models.ExamDetail;
import com.svceindore.examservice.repositories.ExamRepository;
import net.minidev.json.JSONObject;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 3:39 PM
 **/
@RestController
public class ExamController {

    private final KeycloakRestTemplate restTemplate;
    private final ExamRepository examRepository;


    public ExamController(KeycloakRestTemplate restTemplate, ExamRepository examRepository) {
        this.restTemplate = restTemplate;
        this.examRepository = examRepository;
    }

    @RolesAllowed(Roles.ADMIN_ROLE)
    @PostMapping("/create")
    public ResponseEntity<?> createExam(@RequestBody ExamDetail examDetail){
        JSONObject res = new JSONObject();

        if (examDetail.getCourseId()==null||examDetail.getCourseId().isEmpty()){
            res.appendField("status",false);
            res.appendField("message","Valid courseId required.");
            return ResponseEntity.ok(res.toString());
        }

        if (examDetail.getBranchId()==null||examDetail.getBranchId().isEmpty()){
            res.appendField("status",false);
            res.appendField("message","Valid branchId required.");
            return ResponseEntity.ok(res.toString());
        }

        examRepository.insert(examDetail);

        res.appendField("status",true);
        res.appendField("message","Exam created successfully.");
        res.appendField("id",examDetail.getId());
        return ResponseEntity.ok(res.toJSONString());
    }
}
