package com.azkivam.banking.service.transaction;

import com.azkivam.banking.service.bank.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

@Service
@Slf4j
public class TransactionLogger implements TransactionObserver {

    private static final String LOG_FILE = "transactions.log";

    @Override
    public void onTransaction(String accountNumber, String transactionType, BigDecimal amount) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.printf("Account: %s, Type: %s, Amount: %.2f%n", accountNumber, transactionType, amount);
        } catch (IOException e) {
            log.error("transaction logger couldn't save properly", e);
        }
    }
}
