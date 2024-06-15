package com.azkivam.banking.service.bank;

import com.azkivam.banking.exception.BusinessException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BankingOperationFactory {

    private final List<BankingOperationStrategy> services;

    private final Map<TransactionType, BankingOperationStrategy> operationServiceCache = new EnumMap<>(TransactionType.class);

    @PostConstruct
    public void initMyServiceCache() {
        for (BankingOperationStrategy service : services) {
            operationServiceCache.put(service.getType(), service);
        }
    }

    public BankingOperationStrategy getOperationService(TransactionType type) {
        BankingOperationStrategy service = operationServiceCache.get(type);
        if (service == null) throw new BusinessException("the banking operation doesn't support");
        return service;
    }
}
