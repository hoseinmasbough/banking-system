package com.azkivam.banking.service.bank;

import com.azkivam.banking.api.model.TransferResponse;
import com.azkivam.banking.data.account.BankAccount;
import com.azkivam.banking.data.account.BankAccountRepository;
import com.azkivam.banking.service.bank.dto.BankTransferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransferOperationImpl implements BankingOperationStrategy {
    @Override
    public TransactionType getType() {
        return TransactionType.TRANSFER;
    }

    @Override
    public TransferResponse execute(BankTransferDto dto, BankAccountRepository repository) {
        BankAccount sourceAccount = dto.getSourceAccount();
        BankAccount destinationAccount = dto.getDestinationAccount();

        if (sourceAccount.getBalance().compareTo(dto.getAmount()) >= 0) {
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(dto.getAmount()));
            destinationAccount.setBalance(destinationAccount.getBalance().add(dto.getAmount()));
            repository.save(sourceAccount);
            repository.save(destinationAccount);
        } else {
            return new TransferResponse(false, "Insufficient balance");
        }
        log.info("Transfer {} amount from {} to {}", dto.getAmount(), sourceAccount.getAccountNumber(), destinationAccount.getAccountNumber());
        return new TransferResponse(true, "Transfer successful");
    }
}
