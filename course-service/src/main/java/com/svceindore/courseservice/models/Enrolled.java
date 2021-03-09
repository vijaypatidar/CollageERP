package com.svceindore.courseservice.models;

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
@Document
public class Enrolled {
    @Id
    private String id;
    @Indexed
    private String courseId;
    @Indexed
    private String branchId;
    private String studentUsername;
    private String studentName;
    private Date enrollmentDate;

    @Override
    public String toString() {
        return "Enrolled{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", studentUsername='" + studentUsername + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                '}';
    }
}
