package com.svceindore.examservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 8:33 AM
 **/
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Document
public class Result {
    @Id
    private String id;
    @Indexed
    private String examId;
    @Indexed
    private String studentId;
    private boolean present;
    private int markObtain;

    @Override
    public String toString() {
        return "Result{" +
                "id='" + id + '\'' +
                ", examId='" + examId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", present=" + present +
                ", markObtain=" + markObtain +
                '}';
    }
}
