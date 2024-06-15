package com.azkivam.banking.service.bank;

import com.azkivam.banking.api.model.TransferResponse;
import com.azkivam.banking.data.account.BankAccountRepository;
import com.azkivam.banking.service.bank.dto.BankTransferDto;

public interface BankingOperationStrategy {

    TransactionType getType();

    TransferResponse execute(BankTransferDto dto, BankAccountRepository repository);
}
