package com.svceindore.libraryservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by vijay
 * Date: 29/01/21
 * Time: 9:46 AM
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class BookDetail {
    @Id
    private String id;
    private String title;
    private List<String> authors;
    private int totalCopies;
    private int availableCopies;
    private String isbn;//optional
    private String rackId;
    private int rackRow;

    @Override
    public String toString() {
        return "BookDetail{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authors=" + authors +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                ", rackId='" + rackId + '\'' +
                ", rackRow=" + rackRow +
                ", isbn='" + isbn + '\'' +
                '}';
    }
}
