package com.svceindore.uiservice.model.timetables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Date: 16/03/21
 * Time: 7:30 AM
 **/
@NoArgsConstructor
@Getter
@Setter
public class TimeTable {
    private String id;
    private String courseId;
    private String branchId;
    private String sessionId;
    private List<Time> time;
    private List<List<String>> lectures;
    private Date lastUpdatedOn;

    @Override
    public String toString() {
        return "TimeTable{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", branchId='" + branchId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", time=" + time +
                ", lectures=" + lectures +
                ", lastUpdatedOn=" + lastUpdatedOn +
                '}';
    }
}
