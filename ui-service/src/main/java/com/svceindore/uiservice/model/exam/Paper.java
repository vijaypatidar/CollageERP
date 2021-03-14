package com.svceindore.uiservice.model.exam;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Date: 14/03/21
 * Time: 9:44 PM
 **/
@NoArgsConstructor
@Getter
@Setter
public class Paper {
    private String id;
    private List<Question> questions;
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

