package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Set<Transaction> transactionFilter(LocalDateTime from,
                                              LocalDateTime to,
                                              Set<Transaction> transactions) {
        return transactions
                .stream()
                .filter(transaction ->(transaction.getDate()).isAfter(from)
                        && (transaction.getDate()).isBefore(to)).collect(Collectors.toSet());
    }
}
