package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    Set<AccountDTO> accounts = new HashSet<>();

    Set<ClientLoanDTO> loans = new HashSet<>();

    Set<CardDTO> cards = new HashSet<>();

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(item -> new AccountDTO(item)).collect(Collectors.toSet());
        this.loans = client.getClientLoans().stream().map(item -> new ClientLoanDTO(item)).collect(Collectors.toSet());
        this.cards = client.getCard().stream().filter(card -> card.getEnabled() == true).map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getClientLoan(){
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}
