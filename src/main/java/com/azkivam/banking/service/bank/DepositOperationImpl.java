package com.azkivam.banking.service.bank;

import com.azkivam.banking.api.model.TransferResponse;
import com.azkivam.banking.data.account.BankAccount;
import com.azkivam.banking.data.account.BankAccountRepository;
import com.azkivam.banking.service.bank.dto.BankTransferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepositOperationImpl implements BankingOperationStrategy {
    @Override
    public TransactionType getType() {
        return TransactionType.DEPOSIT;
    }

    @Override
    public TransferResponse execute(BankTransferDto dto, BankAccountRepository repository) {
        BankAccount sourceAccount = dto.getSourceAccount();
        sourceAccount.setBalance(sourceAccount.getBalance().add(dto.getAmount()));
        repository.save(sourceAccount);
        log.info("Deposit {} amount from {}", dto.getAmount(), sourceAccount.getAccountNumber());
        return new TransferResponse(true, "Transfer successful");
    }
}
