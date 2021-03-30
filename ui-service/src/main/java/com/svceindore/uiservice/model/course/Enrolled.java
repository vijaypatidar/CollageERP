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
    private String sessionId;
    private String courseName;
    private String branchName;
    private String sessionName;
    private int currentSemester;
    private String studentName;
    private String studentUsername;
    private Date enrollmentDate;

    @Override
    public String toString() {
        return "Enrolled{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", sessionName='" + sessionName + '\'' +
                ", currentSemester=" + currentSemester +
                ", studentName='" + studentName + '\'' +
                ", studentUsername='" + studentUsername + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
