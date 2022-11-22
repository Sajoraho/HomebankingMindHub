package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.stream.Collectors;

public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private Integer payment;
    private String numberAccount;

    ////////////////////////////////

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }
    public Integer getPayment() {
        return payment;
    }
    public String getNumberAccount() {
        return numberAccount;
    }
}
