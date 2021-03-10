package com.svceindore.courseservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Vijay Patidar
 * Date: 10/03/21
 * Time: 1:44 PM
 **/
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Document
public class Session {
    @Id
    private String id;
    private String name;
    private int year;
    private boolean currentSession;

}
