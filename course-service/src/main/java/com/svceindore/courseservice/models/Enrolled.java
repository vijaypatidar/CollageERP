package com.svceindore.courseservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String enrollmentDate;

}