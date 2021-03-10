package com.svceindore.uiservice.model.course;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Vijay Patidar
 * Date: 10/03/21
 * Time: 2:02 PM
 **/
@Getter
@Setter
public class Session {

    private String id;
    private String name;
    private int year;
    private boolean currentSession;

}
