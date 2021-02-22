package com.svceindore.uiservice.model;

import java.util.Date;

/**
 * Created by vijay
 * Date: 29/01/21
 * Time: 10:03 AM
 **/

public class BookHistory {
    private String title;
    private String id;
    private String issuedTo;//username to which book is issued
    private String bid;
    private String bcid;
    private Date issuedOn;
    private Date submittedOn;
    private int fine;

    public BookHistory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getBcid() {
        return bcid;
    }

    public void setBcid(String bcid) {
        this.bcid = bcid;
    }

    public Date getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
    }

    public Date getSubmittedOn() {
        return submittedOn;
    }

    public void setSubmittedOn(Date submittedOn) {
        this.submittedOn = submittedOn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }
}
