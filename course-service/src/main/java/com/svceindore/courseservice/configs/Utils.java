package com.svceindore.courseservice.configs;

import com.svceindore.courseservice.models.Semester;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * Created by Vijay Patidar
 * Date: 10/03/21
 * Time: 6:33 PM
 **/
@Component
@Getter
public class Utils {

    public Utils() {

    }

    private Semester[] semesters = new Semester[]{
            new Semester(1, "First"),
            new Semester(2, "Second"),
            new Semester(3, "Third"),
            new Semester(4, "Fourth"),
            new Semester(5, "Fifth"),
            new Semester(6, "Sixth"),
            new Semester(7, "Seventh"),
            new Semester(8, "Eighth"),
    };
}
