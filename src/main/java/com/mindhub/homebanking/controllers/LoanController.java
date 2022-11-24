package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDTO> getLoanDTO(){
        return loanService.getLoanDTO();
    }

    @Transactional
    @PostMapping("clients/current/loans")
    public ResponseEntity<?> createLoan(Authentication authentication,
                                        @RequestBody LoanApplicationDTO loanApplication){
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        long idLoanApplication = loanApplication.getId();
        Double amountLoanApplication = loanApplication.getAmount();
        Integer paymentLoanApplication = loanApplication.getPayment();
        String numberAccountLoanApplication = loanApplication.getNumberAccount();

        Account findAccount = accountService.findByNumber(numberAccountLoanApplication);
        Loan findLoan = loanService.findById(idLoanApplication);

        Set<Account> accountsCurrent = clientCurrent.getAccounts();
        //Manejo de errores
        if(clientCurrent == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }

        if(findLoan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.FORBIDDEN);
        }

        if(findAccount == null) {
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        if (amountLoanApplication <= 0.0 || amountLoanApplication == null) {
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        if (paymentLoanApplication <= 0 || paymentLoanApplication == null) {
            return new ResponseEntity<>("Invalid payment", HttpStatus.FORBIDDEN);
        }

        if(!accountsCurrent.contains(findAccount)){
            return new ResponseEntity<>("Account does not exist", HttpStatus.FORBIDDEN);
        }

        if(amountLoanApplication > findLoan.getMaxAmount()){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        if (!findLoan.getPayments().contains(paymentLoanApplication) ) {
            return new ResponseEntity<>("Invalid payment", HttpStatus.FORBIDDEN);
        }

        if(findLoan.getName() == "Mortgage"){
            amountLoanApplication *= 1.20;
        }

        if(findLoan.getName() == "Personal"){
            amountLoanApplication *= 1.10;
        }

        if(findLoan.getName() == "Automobile"){
            amountLoanApplication *= 1.15;
        }

        Transaction transactionCredit = new Transaction(TransactionType.CREDIT,
                loanApplication.getAmount(),
                findLoan.getName()+ " Loan approved",
                LocalDateTime.now(),
                findAccount);

        findAccount.setBalance(findAccount.getBalance() + loanApplication.getAmount());

        ClientLoan clientLoan = new ClientLoan(amountLoanApplication,
                paymentLoanApplication,
                clientCurrent,
                findLoan);

        transactionService.saveTransaction(transactionCredit);
        accountService.saveAccount(findAccount);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>("Sucefully transaction", HttpStatus.CREATED);
    }

    @PostMapping("/loan")
    public ResponseEntity<?> createLoan(Authentication authentication,
                                        @RequestBody Loan loan){
        Client clientCurrent = clientService.findByEmail(authentication.getName());

        if(clientCurrent == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }

//        if(clientCurrent.getEmail() != "admin@admin.com"){
//            return new ResponseEntity<>("Client not admin", HttpStatus.FORBIDDEN);
//        }

        if(loan.getName().isEmpty()){
            return new ResponseEntity<>("Name empty", HttpStatus.FORBIDDEN);
        }

        if(loan.getMaxAmount().equals(0) || loan.getMaxAmount().equals(null)){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }

        if(loan.getPayments().isEmpty()){
            return new ResponseEntity<>("Payments empty", HttpStatus.FORBIDDEN);
        }

        Loan createLoan = new Loan(loan.getName(),
                loan.getMaxAmount(),
                loan.getPayments());

        loanService.saveLoan(createLoan);

        return new ResponseEntity<>("Loan created", HttpStatus.CREATED);
    }

}
