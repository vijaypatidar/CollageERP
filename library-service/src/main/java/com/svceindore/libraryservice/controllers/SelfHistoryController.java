package com.svceindore.libraryservice.controllers;

import com.svceindore.libraryservice.models.BookDetail;
import com.svceindore.libraryservice.models.History;
import com.svceindore.libraryservice.models.wrappers.BookHistoryResponse;
import com.svceindore.libraryservice.repositories.BookDetailRepository;
import com.svceindore.libraryservice.repositories.HistoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Vijay Patidar
 * Date: 21/02/21
 * Time: 3:22 PM
 **/
@RestController
@RequestMapping("/history/self/")
public class SelfHistoryController {
    private final HistoryRepository historyRepository;
    private final BookDetailRepository bookDetailRepository;

    public SelfHistoryController(HistoryRepository historyRepository, BookDetailRepository bookDetailRepository) {
        this.historyRepository = historyRepository;
        this.bookDetailRepository = bookDetailRepository;
    }

    @GetMapping("/get_issued")
    public List<BookHistoryResponse> getIssuedHistory(Principal principal) {
        return getHistory(principal.getName(), "issued");
    }

    @GetMapping("/get_submitted")
    public List<BookHistoryResponse> getSubmittedHistory(Principal principal) {
        return getHistory(principal.getName(), "submitted");
    }

    @GetMapping("/get_all")
    public List<BookHistoryResponse> getAllHistory(Principal principal) {
        return getHistory(principal.getName(), "all");
    }


    private List<BookHistoryResponse> getHistory(String username, String type) {
        List<History> histories = historyRepository.findByIssuedTo(username)
                .stream().filter(history -> {
                    if (type.equals("issued")) {
                        return history.getSubmittedOn() == null;
                    } else if (type.equals("submitted")) {
                        return history.getSubmittedOn() != null;
                    }
                    return true;
                }).collect(Collectors.toList());


        List<BookHistoryResponse> responses = new ArrayList<>();
        histories.forEach(history -> responses.add(new BookHistoryResponse(history)));

        responses.forEach(bookHistoryResponse -> {
            Optional<BookDetail> optional = bookDetailRepository.findById(bookHistoryResponse.getBcid());
            optional.ifPresent(bookDetail -> bookHistoryResponse.setTitle(bookDetail.getTitle()));
        });
        return responses;
    }
}
