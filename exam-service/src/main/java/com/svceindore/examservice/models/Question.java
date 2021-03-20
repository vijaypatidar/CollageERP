package com.svceindore.examservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {
    private int id;
    private String question;
    private String imageUrl;
    private List<String> options;
    private int mark;

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", options=" + options +
                ", mark=" + mark +
                '}';
    }
}
