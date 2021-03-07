package com.svceindore.uiservice.model.course;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Vijay Patidar
 * Date: 06/03/21
 * Time: 10:23 AM
 **/
@Getter
@Setter
@NoArgsConstructor
public class Branch {
    private String id;
    private String name;
    private String courseId;
    private String hodUsername;
    private String hodContact;
}
