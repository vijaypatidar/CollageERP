package com.svceindore.uiservice.model.timetables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Date: 16/03/21
 * Time: 7:31 AM
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Time {
    private String start;
    private String end;

    @Override
    public String toString() {
        return "Time{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}
