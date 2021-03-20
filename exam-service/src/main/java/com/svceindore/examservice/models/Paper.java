package com.svceindore.examservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Date: 23/02/21
 * Time: 8:44 AM
 **/
@Document
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paper {
    @Id
    private String id;//same as exam id for which this paper is created i.e foreign key
    private List<Question> questions;
    private List<Answer> answers;
    private int totalMark;

    @Override
    public String toString() {
        return "Paper{" +
                "id='" + id + '\'' +
                ", questions=" + questions +
                ", totalMark=" + totalMark +
                '}';
    }
}

