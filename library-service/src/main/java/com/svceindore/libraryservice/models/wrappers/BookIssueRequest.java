package com.svceindore.libraryservice.models.wrappers;

/**
 * Created by Vijay Patidar
 * Date: 30/01/21
 * Time: 2:58 PM
 **/
public class BookIssueRequest {

    private String bid;//unique book id
    private String username;

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
