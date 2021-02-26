package com.svceindore.libraryservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by vijay
 * Date: 29/01/21
 * Time: 10:03 AM
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document
public class History {
    @Id
    private String id;
    private String issuedTo;//username to which book is issued
    private String bid;
    private String bcid;
    private Date issuedOn;
    private Date submittedOn;
    private int fine;

    public History(History history) {
        this.id=history.id;
        this.bcid=history.bcid;
        this.bid=history.bid;
        this.fine  =history.fine;
        this.issuedOn=history.issuedOn;
        this.issuedTo=history.issuedTo;
        this.submittedOn=history.submittedOn;
    }


}
