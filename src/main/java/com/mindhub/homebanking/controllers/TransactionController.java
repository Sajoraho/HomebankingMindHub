package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.PDFService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PDFService pdfService;

    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<?> createTransaction(Authentication authentication,
                                                @RequestParam Double amount,
                                                @RequestParam String description,
                                                @RequestParam String accountOrigin,
                                                @RequestParam String accountTarget){
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Account accountSender = accountService.findByNumber(accountOrigin);
        Account accountDestination = accountService.findByNumber(accountTarget);
        Set<Account> accountCurrent = clientCurrent.getAccounts();

        if(amount <= 0 || amount == null){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        if(description.isEmpty()){
            return new ResponseEntity<>("Description is empty", HttpStatus.FORBIDDEN);
        }

        if(accountOrigin.isEmpty()){
            return new ResponseEntity<>("Account is empty", HttpStatus.FORBIDDEN);
        }

        if (accountTarget.isEmpty()){
            return new ResponseEntity<>("Account target is empty", HttpStatus.FORBIDDEN);
        }

        if (accountTarget.equals(accountOrigin) || accountOrigin.equals(accountTarget)){
            return new ResponseEntity<>("Accounts are the same", HttpStatus.FORBIDDEN);
        }

        if(accountSender == null){
            return new ResponseEntity<>("Origin Account does not exist", HttpStatus.FORBIDDEN);
        }

        if(accountDestination == null){
            return new ResponseEntity<>("Target Account does not exist", HttpStatus.FORBIDDEN);
        }

        if(accountSender.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        if(!accountCurrent.contains(accountSender)){
            return new ResponseEntity<>("Origin account does not belong to the user", HttpStatus.FORBIDDEN);
        }

        Transaction TransactionSender = new Transaction(TransactionType.DEBIT,
                -amount,
                description,
                LocalDateTime.now(),
                accountSender);

        Transaction TransactionDestination = new Transaction(TransactionType.CREDIT,
                amount,
                description,
                LocalDateTime.now(),
                accountDestination);

        accountSender.setBalance(accountSender.getBalance() - amount);
        accountDestination.setBalance(accountDestination.getBalance() + amount);

        transactionService.saveTransaction(TransactionSender);
        transactionService.saveTransaction(TransactionDestination);

        accountService.saveAccount(accountSender);
        accountService.saveAccount(accountDestination);

        return new ResponseEntity<>("Sucefully transaction", HttpStatus.CREATED);
    }

    LocalDateTime localDateTimeFrom = LocalDateTime.now().minusDays(30);
    String accountNumber;
    LocalDateTime localDateNow = LocalDateTime.now();
    LocalDateTime localDateMinus = LocalDateTime.now().minusDays(30);

    @PostMapping("/pdf")
    public ResponseEntity<?> getPdf(Authentication authentication,
                                    @RequestParam String number,
                                    @RequestParam String localDateMinus){
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        accountNumber = number;
        if(localDateMinus.equals("month")){
            localDateTimeFrom = LocalDateTime.now().minusDays(30);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    };

    @GetMapping("/pdf")
    public void pdf(HttpServletResponse response)throws DocumentException, IOException {

        Account account = accountService.findByNumber(accountNumber);
        Set<Transaction> transactions = account.getTransactions();
        Set<Transaction> transaccionesFiltradas = transactionService.transactionFilter(localDateTimeFrom, localDateNow, transactions);
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValues = "attachment; filename=pdf_Transacciones.pdf";
        response.setHeader(headerKey, headerValues);
        pdfService.export(account, transaccionesFiltradas, response);
    }
}
