package com.azkivam.banking.service.bank;

import com.azkivam.banking.api.model.TransferResponse;
import com.azkivam.banking.data.account.BankAccount;
import com.azkivam.banking.data.account.BankAccountRepository;
import com.azkivam.banking.service.bank.dto.BankTransferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WithdrawOperationImpl implements BankingOperationStrategy {
    @Override
    public TransactionType getType() {
        return TransactionType.WITHDRAW;
    }

    @Override
    public TransferResponse execute(BankTransferDto dto, BankAccountRepository repository) {
        BankAccount sourceAccount = dto.getSourceAccount();
        if (sourceAccount.getBalance().compareTo(dto.getAmount()) >= 0) {
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(dto.getAmount()));
            repository.save(sourceAccount);
        } else {
            return new TransferResponse(false, "Insufficient balance");
        }
        log.info("Withdraw {} amount from {}", dto.getAmount(), sourceAccount.getAccountNumber());
        return new TransferResponse(true, "Withdraw successful");
    }
}
