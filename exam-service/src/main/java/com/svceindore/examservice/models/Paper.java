package com.svceindore.examservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 23/02/21
 * Time: 8:44 AM
 **/
@Document
@NoArgsConstructor
@Getter
@Setter
public class Paper {
    @Id
    private String id;//same as exam id for which this paper is created i.e foreign key
    private List<Question> questions;
    private int totalMark;
}

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
class Question{
    private String question;
    private String imageUrl;
    private List<String> options;
    private int mark;

}