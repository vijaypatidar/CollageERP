package com.svceindore.accountsservice.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransactionRequest {
    private String studentUsername;
    private String courseId;
    private String branchId;
}
