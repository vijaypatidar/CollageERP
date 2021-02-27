package com.svceindore.libraryservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by vijay
 * Date: 29/01/21
 * Time: 9:59 AM
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document
public class Book {
    @Id
    private String id;
    @Indexed
    private String bid;//for getting book detail
    private String issuedTo;//username to which book is issued
    private Date issuedOn;// date when book is issued

}
