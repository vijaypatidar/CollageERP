package com.svceindore.uiservice.model.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private int totalFee;

}
