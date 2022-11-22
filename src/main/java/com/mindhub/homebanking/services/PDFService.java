package com.mindhub.homebanking.services;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public interface PDFService {
    public void export(Account account, Set<Transaction> transactions, HttpServletResponse response) throws IOException, DocumentException;
}
