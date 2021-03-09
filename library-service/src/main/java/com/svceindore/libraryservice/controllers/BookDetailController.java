package com.svceindore.libraryservice.controllers;

import com.svceindore.libraryservice.configs.Roles;
import com.svceindore.libraryservice.models.BookDetail;
import com.svceindore.libraryservice.repositories.BookDetailRepository;
import com.svceindore.libraryservice.repositories.BookRepository;
import net.minidev.json.JSONObject;
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

    @RolesAllowed({Roles.ROLE_LIBRARIAN})
    @PostMapping("/bookDetail")
    public ResponseEntity<?> addBook(@RequestBody BookDetail bookDetail) {
        logger.info("Add book detail " + bookDetail.toString());
        JSONObject res = new JSONObject();
        res.appendField("status",false);
        if (bookDetail.getTitle() == null || bookDetail.getTitle().isEmpty()) {
            res.appendField("message","Book title required.");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(res.toString());
        }
        if (bookDetail.getAuthors() == null || bookDetail.getAuthors().isEmpty()) {
            res.appendField("message","At least one author name required.");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(res.toString());
        }

        bookDetailRepository.insert(bookDetail);

        res.appendField("status",true);
        res.appendField("message","Book added successfully.");
        res.appendField("id",bookDetail.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
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
