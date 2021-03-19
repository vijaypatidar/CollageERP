package com.svceindore.examservice.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Vijay Patidar
 * Date: 19/03/21
 * Time: 2:16 PM
 **/
@Document
@Getter
@Setter
public class Solution {
    @Id
    private String id;
    private String studentId;
    private String paperId;
    private List<Answer> answers;

}
