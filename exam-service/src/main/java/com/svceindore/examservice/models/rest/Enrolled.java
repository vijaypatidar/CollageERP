package com.svceindore.examservice.models.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Vijay Patidar
 * Date: 27/02/21
 * Time: 4:58 PM
 **/

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Enrolled {
    private String id;
    private String courseId;
    private String sessionId;
    private String branchId;
    private String studentUsername;
    private String studentName;
    private Date enrollmentDate;
    private int currentSemester;
}
