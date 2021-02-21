package com.svceindore.libraryservice.models.wrappers;

import com.svceindore.libraryservice.models.History;

/**
 * Created by Vijay Patidar
 * Date: 21/02/21
 * Time: 3:20 PM
 **/
public class BookHistoryResponse extends History {
    private String title;

    public BookHistoryResponse(History history) {
        super(history);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
