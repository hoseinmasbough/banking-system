package com.azkivam.banking.service.bank.dto;

import com.azkivam.banking.data.account.BankAccount;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class BankTransferDto {

    private BankAccount sourceAccount;
    private BankAccount destinationAccount;
    private BigDecimal amount;
}
