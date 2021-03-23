package com.svceindore.examservice.models.rest;

import com.svceindore.examservice.models.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 8:33 AM
 **/
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ResultRest extends com.svceindore.examservice.models.Result {

    private String examTitle;
    private String examSubject;

}
