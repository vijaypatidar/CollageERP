package com.svceindore.examservice.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * Created by Vijay Patidar
 * Date: 19/03/21
 * Time: 2:18 PM
 **/
@Getter
@Setter
public class Answer {
    private int id;
    private String value;
}
