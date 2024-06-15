package com.azkivam.banking.service.bank.mapper;

import com.azkivam.banking.data.account.BankAccount;
import com.azkivam.banking.service.bank.dto.BankTransferDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BankTransferMapper {

    BankTransferDto toBankTransferDto(BankAccount sourceAccount, BankAccount destinationAccount, BigDecimal amount);

    BankTransferDto toBankTransferDto(BankAccount sourceAccount, BigDecimal amount);

}
