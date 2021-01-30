package com.svceindore.libraryservice.controllers;

import com.svceindore.libraryservice.configs.Roles;
import com.svceindore.libraryservice.models.Book;
import com.svceindore.libraryservice.models.BookDetail;
import com.svceindore.libraryservice.models.History;
import com.svceindore.libraryservice.models.wrappers.BookIssueRequest;
import com.svceindore.libraryservice.repositories.BookDetailRepository;
import com.svceindore.libraryservice.repositories.BookRepository;
import com.svceindore.libraryservice.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/library")
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

    @RolesAllowed({Roles.ADMIN_LIBRARIAN, Roles.ADMIN_ROLE})
    @PostMapping("/bookCopy")
    public ResponseEntity<?> addBookCopy(@RequestBody Book book) {

        if (book.getBid() == null || book.getBid().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("Book id(bid) required.");
        }

        //check for valid id
        if (book.getId() == null || book.getId().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body("Book id required.");
        }

        //check if book with this id already exists
        if (bookRepository.findById(book.getId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Book already exits with id = " + book.getId());
        }


        Optional<BookDetail> bookDetailOptional = bookDetailRepository.findById(book.getBid());
        if (bookDetailOptional.isPresent()) {
            BookDetail bookDetail = bookDetailOptional.get();

            //updating book count
            bookDetail.setTotalCopies(bookDetail.getTotalCopies() + 1);
            bookDetail.setAvailableCopies(bookDetail.getAvailableCopies() + 1);
            bookRepository.save(book);
            bookDetailRepository.save(bookDetail);
            return ResponseEntity.status(HttpStatus.CREATED).body("Book copy added, id = " + book.getId());
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Unable add book, invalid bid");
        }
    }

    @RolesAllowed({Roles.ADMIN_LIBRARIAN, Roles.ADMIN_ROLE})
    @PostMapping("/issueBook")
    public ResponseEntity<?> issueBook(@RequestBody BookIssueRequest bookIssueRequest) {
        String bid = bookIssueRequest.getBid();
        Optional<Book> bookOptional = bookRepository.findById(bid);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getIssuedTo() != null && !book.getIssuedTo().isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("Book already issued to " + book.getIssuedTo());
            } else {
                book.setIssuedTo(bookIssueRequest.getUsername());
                book.setIssuedOn(new Date());
                bookRepository.save(book);

                History history = new History();
                history.setBid(book.getId());
                history.setBcid(book.getBid());
                history.setIssuedOn(book.getIssuedOn());
                history.setIssuedTo(book.getIssuedTo());

                //save to history
                historyRepository.insert(history);

                return ResponseEntity.status(HttpStatus.OK).body("Book issued");
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Book not found with this code");
        }
    }

    @RolesAllowed({Roles.ADMIN_LIBRARIAN, Roles.ADMIN_ROLE})
    @PostMapping("/submitBook")
    public ResponseEntity<?> submitBook(@RequestBody BookIssueRequest bookIssueRequest) {
        String bid = bookIssueRequest.getBid();
        if (bid==null||bid.isEmpty()){
            return ResponseEntity.ok().body("Book id required.");
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
                int days = (int) ((history.getSubmittedOn().getTime() - history.getIssuedOn().getTime()) /divideBy);

                if (days>bookIssuedForDays){
                    int fine = finePerDay*(days-bookIssuedForDays);
                    history.setFine(fine);
                }

                //saving updated info
                historyRepository.save(history);

                return ResponseEntity.status(HttpStatus.OK).body("Book submitted");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Book is not issued.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Book not found with this code");
        }
    }


}
