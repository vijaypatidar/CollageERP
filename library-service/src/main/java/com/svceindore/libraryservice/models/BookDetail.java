package com.svceindore.libraryservice.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by vijay
 * Date: 29/01/21
 * Time: 9:46 AM
 **/
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


    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public int getRackRow() {
        return rackRow;
    }

    public void setRackRow(int rackRow) {
        this.rackRow = rackRow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

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
