package com.svceindore.accountsservice.repositories;

import com.svceindore.accountsservice.models.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
     List<Transaction> findAllByStudentUsername(String studentUsername);

     List<Transaction> findAllByStudentUsernameAndStatus(String name, String status);
}
