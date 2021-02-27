package com.svceindore.libraryservice.models.wrappers;

import com.svceindore.libraryservice.models.History;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Vijay Patidar
 * Date: 21/02/21
 * Time: 3:20 PM
 **/
@Getter
@Setter
public class BookHistoryResponse extends History {
    private String title;

    public BookHistoryResponse(History history) {
        super(history);
    }
}
