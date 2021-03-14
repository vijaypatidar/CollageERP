package com.svceindore.examservice.models.rest;

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
public class Course {
    private String id;
    private String name;
    private int duration;
    private int feePerYear;
    private boolean active;
    private int totalFee;

}
