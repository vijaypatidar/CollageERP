package com.svceindore.uiservice.model.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Vijay Patidar
 * Date: 11/03/21
 * Time: 10:42 AM
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private String id;
    private String courseId;
    private String name;
}
