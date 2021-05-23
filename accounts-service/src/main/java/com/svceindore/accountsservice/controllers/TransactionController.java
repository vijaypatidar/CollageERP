package com.svceindore.accountsservice.controllers;

import com.svceindore.accountsservice.configs.Roles;
import com.svceindore.accountsservice.models.CreateTransactionRequest;
import com.svceindore.accountsservice.models.Transaction;
import com.svceindore.accountsservice.repositories.TransactionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class TransactionController {

    private final TransactionRepository repository;

    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/transactions")
    public List<Transaction> getMyTransactions(Principal principal, @RequestParam(required = false, defaultValue = "all") String status) {
        if (status.equals("all"))
            return repository.findAllByStudentUsername(principal.getName());
        else
            return repository.findAllByStudentUsernameAndStatus(principal.getName(), status);
    }

    @RolesAllowed({Roles.ROLE_ACCOUNTANT})
    @GetMapping("/transactions/{studentUsername}")
    public List<Transaction> getTransactions(@PathVariable String studentUsername, @RequestParam(required = false, defaultValue = "all") String status) {
        if (status.equals("all"))
            return repository.findAllByStudentUsername(studentUsername);
        else
            return repository.findAllByStudentUsernameAndStatus(studentUsername, status);
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> createTransactions(@RequestBody @Valid CreateTransactionRequest createTransactionRequest) {

        return null;
    }

}
