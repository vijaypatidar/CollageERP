package com.svceindore.uiservice.model.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Created by Vijay Patidar
 * Date: 07/03/21
 * Time: 1:38 PM
 **/

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Enrolled {
    private String id;
    private String courseId;
    private String branchId;
    private String studentName;
    private String studentUsername;
    private Date enrollmentDate;

}
