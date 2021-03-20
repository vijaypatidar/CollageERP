package com.svceindore.libraryservice.controllers;

import com.svceindore.libraryservice.configs.Roles;
import com.svceindore.libraryservice.models.Book;
import com.svceindore.libraryservice.models.BookDetail;
import com.svceindore.libraryservice.models.History;
import com.svceindore.libraryservice.models.wrappers.BookIssueRequest;
import com.svceindore.libraryservice.repositories.BookDetailRepository;
import com.svceindore.libraryservice.repositories.BookRepository;
import com.svceindore.libraryservice.repositories.HistoryRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Date;
import java.util.Optional;

/**
 * Created by Vijay Patidar
 * Date: 30/01/21
 * Time: 12:11 PM
 **/
@RestController
public class BookController {

    private final BookRepository bookRepository;
    private final BookDetailRepository bookDetailRepository;
    private final HistoryRepository historyRepository;
    @Value("${issueBookForDays:30}")
    private int bookIssuedForDays;
    @Value("${finePerDay:5}")
    private int finePerDay;

    public BookController(BookRepository bookRepository, BookDetailRepository bookDetailRepository, HistoryRepository historyRepository) {
        this.bookRepository = bookRepository;
        this.bookDetailRepository = bookDetailRepository;
        this.historyRepository = historyRepository;
    }

    @RolesAllowed({Roles.ROLE_LIBRARIAN})
    @PostMapping("/bookCopy")
    public ResponseEntity<?> addBookCopy(@RequestBody Book book) {
        JSONObject response = new JSONObject();
        response.appendField("status", false);
        if (book.getBid() == null || book.getBid().isEmpty()) {
            response.appendField("message", "Book id(bid) required.");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response.toString());
        }

        //check for valid id
        if (book.getId() == null || book.getId().isEmpty()) {
            response.appendField("message", "Book id required.");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response.toString());
        }

        //check if book with this id already exists
        if (bookRepository.findById(book.getId()).isPresent()) {
            response.appendField("message", "Book already exits with id = " + book.getId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response.toString());
        }


        Optional<BookDetail> bookDetailOptional = bookDetailRepository.findById(book.getBid());
        if (bookDetailOptional.isPresent()) {
            BookDetail bookDetail = bookDetailOptional.get();

            //updating book count
            bookDetail.setTotalCopies(bookDetail.getTotalCopies() + 1);
            bookDetail.setAvailableCopies(bookDetail.getAvailableCopies() + 1);
            bookRepository.save(book);
            bookDetailRepository.save(bookDetail);
            response.appendField("status", true);
            response.appendField("message", "Book copy added, id = " + book.getId());
            response.appendField("id",book.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response.toString());
        } else {
            response.appendField("message", "Unable add book, invalid bid");
            return ResponseEntity.status(HttpStatus.OK).body(response.toString());
        }
    }

    @RolesAllowed({Roles.ROLE_LIBRARIAN})
    @PostMapping("/issueBook")
    public ResponseEntity<?> issueBook(@RequestBody BookIssueRequest bookIssueRequest) {
        JSONObject response = new JSONObject();
        response.appendField("status", false);
        String bid = bookIssueRequest.getBid();
        Optional<Book> bookOptional = bookRepository.findById(bid);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getIssuedTo() != null && !book.getIssuedTo().isEmpty()) {
                response.appendField("message", "Book already issued to " + book.getIssuedTo());
            } else {
                book.setIssuedTo(bookIssueRequest.getUsername());
                book.setIssuedOn(new Date());
                bookRepository.save(book);

                History history = new History();
                history.setBid(book.getBid());
                history.setBcid(book.getId());
                history.setIssuedOn(book.getIssuedOn());
                history.setIssuedTo(book.getIssuedTo());

                //save to history
                historyRepository.insert(history);

                incrementAvailableBookCountBy(book.getBid(), -1);

                response.appendField("status", true);
                response.appendField("message", "Book issued to " + bookIssueRequest.getUsername());
            }
        } else {
            response.appendField("message", "Book not found with this code");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }

    @RolesAllowed({Roles.ROLE_LIBRARIAN})
    @PostMapping("/submitBook")
    public ResponseEntity<?> submitBook(@RequestBody BookIssueRequest bookIssueRequest) {
        JSONObject res = new JSONObject();
        res.appendField("status", false);

        String bid = bookIssueRequest.getBid();
        if (bid == null || bid.isEmpty()) {
            res.appendField("message","Book id required.");
            return ResponseEntity.ok().body(res.toString());
        }

        Optional<Book> bookOptional = bookRepository.findById(bid);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getIssuedTo() != null && !book.getIssuedTo().isEmpty()) {
                History history = historyRepository.findByBidAndSubmittedOn(book.getId(), null);
                //submit book
                book.setIssuedTo(null);
                bookRepository.save(book);

                //calculate fine and update history
                history.setSubmittedOn(new Date());
                long divideBy = 86400000;//24*60*60*1000
                int days = (int) ((history.getSubmittedOn().getTime() - history.getIssuedOn().getTime()) / divideBy);

                if (days > bookIssuedForDays) {
                    int fine = finePerDay * (days - bookIssuedForDays);
                    history.setFine(fine);
                }

                incrementAvailableBookCountBy(book.getBid(), 1);
                //saving updated info
                historyRepository.save(history);

                res.appendField("status",true);
                res.appendField("message","Book submitted successfully.");
                return ResponseEntity.status(HttpStatus.OK).body(res.toString());
            } else {
                res.appendField("message","Book is not issued.");
                return ResponseEntity.status(HttpStatus.OK).body(res.toString());
            }
        } else {
            res.appendField("message","Book not found with this code");
            return ResponseEntity.status(HttpStatus.OK).body(res.toString());
        }
    }


    private void incrementAvailableBookCountBy(String bid, int n) {
        Optional<BookDetail> optional = bookDetailRepository.findById(bid);
        if (optional.isPresent()) {
            BookDetail book = optional.get();
            book.setAvailableCopies(book.getAvailableCopies() + n);
            bookDetailRepository.save(book);
        }
    }


}
