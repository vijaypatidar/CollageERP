package com.svceindore.courseservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Vijay Patidar
 * Date: 27/02/21
 * Time: 4:53 PM
 **/
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document
public class Branch {
    @Id
    private String id;
    private String name;
    private String courseId;
    private String hodName;
    private String hodContact;


}
