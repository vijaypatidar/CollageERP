package com.svceindore.uiservice.model.exam;

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
    private int id;
    private String question;
    private String imageUrl;
    private List<String> options;
    private int mark;

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", options=" + options +
                ", mark=" + mark +
                '}';
    }
}
