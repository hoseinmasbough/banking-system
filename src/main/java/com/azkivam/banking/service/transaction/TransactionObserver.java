package com.azkivam.banking.service.transaction;

import java.math.BigDecimal;

public interface TransactionObserver {
    void onTransaction(String accountNumber, String transactionType, BigDecimal amount);
}
