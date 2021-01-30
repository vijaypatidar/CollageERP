package com.svceindore.libraryservice.controllers;

import com.svceindore.libraryservice.configs.Roles;
import com.svceindore.libraryservice.models.BookDetail;
import com.svceindore.libraryservice.repositories.BookDetailRepository;
import com.svceindore.libraryservice.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

/**
 * Created by Vijay Patidar
 * Date: 30/01/21
 * Time: 12:11 PM
 **/
@RestController
@RequestMapping("/api/library")
public class BookDetailController {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public BookDetailController(BookRepository bookRepository, BookDetailRepository bookDetailRepository) {
        this.bookRepository = bookRepository;
        this.bookDetailRepository = bookDetailRepository;
    }

    @RolesAllowed({Roles.ADMIN_LIBRARIAN, Roles.ADMIN_ROLE})
    @PostMapping("/bookDetail")
    public ResponseEntity<?> addBook(@RequestBody BookDetail bookDetail) {
        if (bookDetail.getTitle() == null || bookDetail.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("Book title required.");
        }
        if (bookDetail.getAuthors() == null || bookDetail.getAuthors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("At least one author name required.");
        }

        bookDetailRepository.insert(bookDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Book Detail added, id = " + bookDetail.getId());
    }

}
