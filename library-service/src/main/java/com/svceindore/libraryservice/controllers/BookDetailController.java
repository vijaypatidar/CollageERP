package com.svceindore.libraryservice.controllers;

import com.svceindore.libraryservice.configs.Roles;
import com.svceindore.libraryservice.models.BookDetail;
import com.svceindore.libraryservice.repositories.BookDetailRepository;
import com.svceindore.libraryservice.repositories.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by Vijay Patidar
 * Date: 30/01/21
 * Time: 12:11 PM
 **/
@RestController
public class BookDetailController {

    private Logger logger = Logger.getLogger(getClass().getCanonicalName());
    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;

    public BookDetailController(BookRepository bookRepository, BookDetailRepository bookDetailRepository) {
        this.bookRepository = bookRepository;
        this.bookDetailRepository = bookDetailRepository;
    }

    @RolesAllowed({Roles.ADMIN_LIBRARIAN, Roles.ADMIN_ROLE})
    @PostMapping("/bookDetail")
    public ResponseEntity<?> addBook(@RequestBody BookDetail bookDetail) {
        logger.info("Add book deatil " + bookDetail.toString());
        if (bookDetail.getTitle() == null || bookDetail.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("Book title required.");
        }
        if (bookDetail.getAuthors() == null || bookDetail.getAuthors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("At least one author name required.");
        }

        bookDetailRepository.insert(bookDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDetail.getId());
    }

    @GetMapping("/bookDetail")
    public List<BookDetail> getBookDetails() {
        return bookDetailRepository.findAllByOrderByTitle();
    }

    @GetMapping("/bookDetail/{bookId}")
    public ResponseEntity<?> getBookDetail(@PathVariable String bookId) {
        Optional<BookDetail> optional = bookDetailRepository.findById(bookId);
        if (optional.isPresent()){
            return ResponseEntity.ok(optional.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(path = "/bookDetail?search")
    public List<BookDetail> searchAndGetBookDetails() {
        return bookDetailRepository.findAll();
    }

}
