package com.svceindore.accountsservice.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Transaction {
    @Id
    private String id;
    private String studentUsername;
    private String enrollmentId;
    private Date transactionDate;
    private String status;
    private String paymentMode;
    private String paymentNote;
    private String semesterId;

}
