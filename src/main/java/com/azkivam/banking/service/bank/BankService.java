package com.azkivam.banking.service.bank;

import com.azkivam.banking.api.model.TransferResponse;
import com.azkivam.banking.data.account.BankAccount;
import com.azkivam.banking.data.account.BankAccountRepository;
import com.azkivam.banking.exception.AccountNotFoundException;
import com.azkivam.banking.service.bank.mapper.BankTransferMapper;
import com.azkivam.banking.service.transaction.TransactionLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class BankService {
    private final BankAccountRepository repository;
    private final TransactionLogger transactionLogger;
    private final ExecutorService executorService;
    private final BankingOperationFactory factory;
    private final BankTransferMapper mapper;

    public String createAccount(String accountHolderName, BigDecimal initialBalance) throws ExecutionException, InterruptedException {
        Future<String> accountNumber = executorService.submit(() -> {
            log.info("Creating account {} with initial balance {}", accountHolderName, initialBalance);
            BankAccount account = new BankAccount();
            account.setAccountHolderName(accountHolderName);
            account.setBalance(initialBalance);
            account.setAccountNumber(generateAccountNumber());
            repository.save(account);
            transactionLogger.onTransaction(account.getAccountNumber(), "ACCOUNT_CREATION", initialBalance);
            return account.getAccountNumber();
        });
        return accountNumber.get();
    }

    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    @Transactional
    public TransferResponse deposit(String accountNumber, BigDecimal amount) {
        Future<TransferResponse> responseFuture = executorService.submit(() -> {
            BankingOperationStrategy operationService = factory.getOperationService(TransactionType.DEPOSIT);
            TransferResponse response = operationService.execute(mapper.toBankTransferDto(getBankAccount(accountNumber), amount), repository);
            if (response.isSuccess()) {
                transactionLogger.onTransaction(accountNumber, "DEPOSIT", amount);
            }
            return response;
        });
        try {
            return responseFuture.get();
        } catch (OptimisticLockingFailureException e) {
            log.error("Optimistic locking failure", e);
            throw e; // Retry will handle this
        } catch (Exception e) {
            return new TransferResponse(false, "An error occurred: " + e.getMessage());
        }
    }

    @Transactional
    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    public TransferResponse withdraw(String accountNumber, BigDecimal amount) {
        Future<TransferResponse> responseFuture = executorService.submit(() -> {
            BankingOperationStrategy operationService = factory.getOperationService(TransactionType.WITHDRAW);
            TransferResponse response = operationService.execute(mapper.toBankTransferDto(getBankAccount(accountNumber), amount), repository);
            if (response.isSuccess()) {
                transactionLogger.onTransaction(accountNumber, "WITHDRAW", amount);
            }
            return response;
        });
        try {
            return responseFuture.get();
        } catch (OptimisticLockingFailureException e) {
            log.error("Optimistic locking failure", e);
            throw e; // Retry will handle this
        } catch (Exception e) {
            return new TransferResponse(false, "An error occurred: " + e.getMessage());
        }
    }

    @Transactional
    @Retryable(
            retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 1000)
    )
    public TransferResponse transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Future<TransferResponse> responseFuture = executorService.submit(() -> {
            BankingOperationStrategy operationService = factory.getOperationService(TransactionType.TRANSFER);
            TransferResponse response = operationService.execute(mapper.toBankTransferDto(getBankAccount(fromAccountNumber), getBankAccount(toAccountNumber), amount), repository);
            if (response.isSuccess()) {
                transactionLogger.onTransaction(fromAccountNumber, "TRANSFER_OUT", amount);
                transactionLogger.onTransaction(toAccountNumber, "TRANSFER_IN", amount);
            }
            return response;
        });
        try {
            return responseFuture.get();
        } catch (OptimisticLockingFailureException e) {
            log.error("Optimistic locking failure", e);
            throw e; // Retry will handle this
        } catch (Exception e) {
            return new TransferResponse(false, "An error occurred: " + e.getMessage());
        }
    }

    public BigDecimal getBalance(String accountNumber) {
        log.debug("Getting balance for account {}", accountNumber);
        return getBankAccount(accountNumber).getBalance();
    }

    private BankAccount getBankAccount(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found : " + accountNumber));
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}
