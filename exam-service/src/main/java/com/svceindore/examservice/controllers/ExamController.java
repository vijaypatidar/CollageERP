package com.svceindore.examservice.controllers;

import com.svceindore.examservice.configs.Roles;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 3:39 PM
 **/
@RestController
public class ExamController {

    @RolesAllowed(Roles.ADMIN_ROLE)
    @PostMapping("/create")
    public ResponseEntity<?> createExam(){
        JSONObject res = new JSONObject();
        res.appendField("id","creajfk");
        return ResponseEntity.ok(res.toJSONString());
    }
}
