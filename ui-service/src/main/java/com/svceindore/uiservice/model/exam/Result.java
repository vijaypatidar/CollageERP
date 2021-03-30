package com.svceindore.uiservice.model.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date: 23/03/21
 * Time: 8:33 AM
 **/
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Result {
    private String id;
    private String examId;
    private String examTitle;
    private String examSubject;
    private String studentId;
    private boolean present;
    private int markObtain;

}
