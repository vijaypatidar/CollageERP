package com.svceindore.courseservice.models.timetables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Date: 16/03/21
 * Time: 7:30 AM
 **/
@Document
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
