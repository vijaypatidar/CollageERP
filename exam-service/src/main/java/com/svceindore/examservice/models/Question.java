package com.svceindore.examservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Question {
    private String question;
    private String imageUrl;
    private List<String> options;
    private int mark;

}
