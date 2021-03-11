package com.svceindore.courseservice.controllers;

import com.svceindore.courseservice.configs.Utils;
import com.svceindore.courseservice.models.Semester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Vijay Patidar
 * Date: 10/03/21
 * Time: 6:36 PM
 **/
@RestController
public class SemesterController {
    private final Utils utils;

    public SemesterController(Utils utils) {
        this.utils = utils;
    }

    @GetMapping("semesters")
    public Semester[] getSemesters(){
        return utils.getSemesters();
    }
}
