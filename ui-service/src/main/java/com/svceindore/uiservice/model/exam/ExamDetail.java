package com.svceindore.uiservice.model.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ExamDetail {
    private String id;
    private String courseId;
    private String branchId;
    private String title;
    private String subjectId;
    private int semester;
    private int duration;
    private Date scheduledOn;
    private boolean onlineMode;
    private boolean resultDeclared;
    private int totalMark;
    private String sessionId;

    @Override
    public String toString() {
        return "ExamDetail{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", title='" + title + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", semester=" + semester +
                ", duration=" + duration +
                ", scheduledOn=" + scheduledOn +
                ", onlineMode=" + onlineMode +
                ", resultDeclared=" + resultDeclared +
                ", totalMark=" + totalMark +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
