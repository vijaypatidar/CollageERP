package com.svceindore.examservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private boolean onlineMode;
    private boolean resultDeclared;
    private int totalMark;

}
