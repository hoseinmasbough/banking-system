package com.azkivam.banking.api;

import com.azkivam.banking.api.model.TransferResponse;
import com.azkivam.banking.service.bank.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("v1/api/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@RequestParam String accountHolderName, @RequestParam BigDecimal initialBalance) throws ExecutionException, InterruptedException {
        String account = bankService.createAccount(accountHolderName, initialBalance);
        return ResponseEntity.status(201).body(account);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransferResponse> deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(bankService.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransferResponse> withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(bankService.withdraw(accountNumber, amount));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(bankService.transfer(fromAccountNumber, toAccountNumber, amount));
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam String accountNumber) {
        return ResponseEntity.ok(bankService.getBalance(accountNumber));
    }
}
