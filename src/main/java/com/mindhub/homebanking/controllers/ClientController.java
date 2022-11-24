package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController //trabaja con las restricciones de rest
@RequestMapping("/api") //se asocia una peticion a un endpoint
public class ClientController {

    @Autowired //A traves de la inyección de depedendencia, permite usar el repositorio creando una instancia
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

//    @RequestMapping("/clients")
    @GetMapping("/clients")
    public List<ClientDTO> getClientDTO() {
        return clientService.getClientDTO();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable long id) {
        return clientService.getClientDTO(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAll(Authentication authentication) {
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }

//    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing first name", HttpStatus.FORBIDDEN);
        }

        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing last name", HttpStatus.FORBIDDEN);
        }

        if (email.isEmpty()) {
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);
        }

        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing password", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) != null) {
            return new ResponseEntity<>("Email is already in use", HttpStatus.FORBIDDEN);
        }

        Client clientCreated = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        clientService.saveClient(clientCreated);

        String accountNumber = "VIN"+getRandomNumber();
        Account account = new Account(accountNumber,
                LocalDateTime.now(),
                0.0,
                AccountType.SAVINGS,
                clientCreated);

        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public long getRandomNumber() {
        return (long) ((Math.random() * (100000001 - 1)) + 1);
    }

}
