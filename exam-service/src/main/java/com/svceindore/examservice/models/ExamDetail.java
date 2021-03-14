package com.svceindore.examservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 8:37 AM
 **/
@Document
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ExamDetail {
    @Id
    private String id;
//    private String examGroupId;
    private String courseId;
    private String branchId;
    private String sessionId;
    private String title;
    private String subjectId;
    private int semester;
    private int duration;
    private Date scheduledOn;
    private boolean onlineMode;
    private boolean resultDeclared;
    private int totalMark;

    @Override
    public String toString() {
        return "ExamDetail{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", title='" + title + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", semester=" + semester +
                ", duration=" + duration +
                ", scheduledOn=" + scheduledOn +
                ", onlineMode=" + onlineMode +
                ", resultDeclared=" + resultDeclared +
                ", totalMark=" + totalMark +
                '}';
    }
}
