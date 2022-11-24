package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccountDTO() {
        return accountService.getAccountDTO();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable long id){
        return accountService.getAccountDTO(id);
    }

    public long getRandomNumber() {
        return (long) ((Math.random() * (100000001 - 1)) + 1);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<?> createAccount(Authentication authentication){
        Client clientCurrent = clientService.findByEmail(authentication.getName());

        if(clientCurrent.getAccounts().size() >= 3){
            return new ResponseEntity<>("You cannot have more than 3 accounts", HttpStatus.FORBIDDEN);
        }

            String accountNumber = AccountUtils.getNumberAccount();
            Account account = new Account(accountNumber,
                    LocalDateTime.now(),
                    0.0,
                    clientCurrent);

            accountService.saveAccount(account);

            return new ResponseEntity<>("Account created", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/account/delete/{id}")
    public ResponseEntity<?> deleteAccount(Authentication authentication,
                                           @PathVariable Long id){
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Set<Account> accountCurrent = clientCurrent.getAccounts();

        if(clientCurrent == null){
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }
        if(id == 0 || id == null){
            return new ResponseEntity<>("Invalid id", HttpStatus.FORBIDDEN);
        }

        Account accountDelete = accountService.findById(id);

        if (accountDelete == null){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        if (!accountCurrent.contains(accountDelete)){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        accountDelete.setEnabled(false);
        accountService.saveAccount(accountDelete);

        return new ResponseEntity<>("Account deleted", HttpStatus.OK);
    }

}
