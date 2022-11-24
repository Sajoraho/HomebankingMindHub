package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);

	}
//	@Autowired
//	PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository){
		return args -> {

//			Client client1 = new Client("Santiago","Rojas","sajoraho@gmail.com", passwordEncoder.encode("1234"));
//			Client client2 = new Client("Pedro","Peréz","pedro@perez.com", passwordEncoder.encode("1234"));
//			Client client3 = new Client("Melba","Morel","melba@mindhub.com", passwordEncoder.encode("1234"));
//			Client admin = new Client("Admin", "Admin", "admin@admin.com", passwordEncoder.encode("1234"));
//
//			clientRepository.save(client1);
//			clientRepository.save(client2);
//			clientRepository.save(client3);
//			clientRepository.save(admin);
//
//			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.00, AccountType.CHECKING, client1);
//			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00, AccountType.SAVINGS,  client1); // Account Constructor 2
//			Account account3 = new Account("VIN003", LocalDateTime.now().plusDays(2), 4000.00, AccountType.SAVINGS, client3);
//			Account account4 = new Account("VIN004", LocalDateTime.now().plusDays(-1), 10000.00, AccountType.CHECKING, client3);
//			Account account5 = new Account("VIN005", LocalDateTime.now().plusDays(-2), 6000.00, AccountType.SAVINGS, client3); // Account Constructor
//			Account account6 = new Account("VIN006", LocalDateTime.now().plusDays(-5), 1000.00, AccountType.CHECKING, client3);
//
////			client1.addAccount(account1); //accounts.add(account);
////			client3.addAccount(account3);
////			client3.addAccount(account4);
////			client3.addAccount(account5);
////			client3.addAccount(account6);
//
//			accountRepository.save(account1);
//			accountRepository.save(account2);
//			accountRepository.save(account3);
//			accountRepository.save(account4);
//			accountRepository.save(account5);
//			accountRepository.save(account6);
//
//			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 200.00, "Food", LocalDateTime.now(), account3);
//			Transaction transaction2 = new Transaction(TransactionType.CREDIT, 100.00, "Services", LocalDateTime.now(), account3);
//			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -50.00, "Clothes", LocalDateTime.now(), account3);
//			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -200.00, "Food", LocalDateTime.now(), account4);
//			Transaction transaction5 = new Transaction(TransactionType.DEBIT, -100.00, "Services", LocalDateTime.now(), account4);
//			Transaction transaction6 = new Transaction(TransactionType.CREDIT, 50.00, "Clothes", LocalDateTime.now(), account5);
//
////			account3.addTransaction(transaction1);
////			account3.addTransaction(transaction2);
////			account3.addTransaction(transaction3);
////			account4.addTransaction(transaction4);
////			account4.addTransaction(transaction5);
////			account5.addTransaction(transaction6);
//
//			transactionRepository.save(transaction1);
//			transactionRepository.save(transaction2);
//			transactionRepository.save(transaction3);
//			transactionRepository.save(transaction4);
//			transactionRepository.save(transaction5);
//			transactionRepository.save(transaction6);
//
//			List<Integer> payment1 = List.of(12,24,36,48,60);
//			List<Integer> payment2 = List.of(6,12,24);
//			List<Integer> payment3 = List.of(6,12,24,36);
//
//			Loan loan1 = new Loan("Mortgage", 500000.00, payment1);
//			Loan loan2 = new Loan("Personal",100000.00, payment2);
//			Loan loan3 = new Loan("Automobile", 300000.00, payment3);
//
//			loanRepository.save(loan1);
//			loanRepository.save(loan2);
//			loanRepository.save(loan3);
//
//			ClientLoan clientLoan1 = new ClientLoan(400000.00, 60, client3, loan1);
//			ClientLoan clientLoan2 = new ClientLoan(500000.00, 12, client3, loan2);
//			ClientLoan clientLoan3 = new ClientLoan(100000.00, 24, client1, loan2);
//			ClientLoan clientLoan4 = new ClientLoan(200000.00, 36, client1, loan3);
//
//			clientLoanRepository.save(clientLoan1);
//			clientLoanRepository.save(clientLoan2);
//			clientLoanRepository.save(clientLoan3);
//			clientLoanRepository.save(clientLoan4);
//
//			Card card1 = new Card(client3.nombreCompleto(), CardType.DEBIT, CardColor.GOLD, "1234567891234567", 123, LocalDate.now().plusYears(5), LocalDate.now(), client3);
//			Card card2 = new Card(client3.nombreCompleto(), CardType.CREDIT, CardColor.TITANIUM, "9876543219876543", 321, LocalDate.now().plusYears(5), LocalDate.now(), client3);
//			Card card3 = new Card(client1.nombreCompleto(), CardType.CREDIT, CardColor.SILVER, "4567891231237894", 456, LocalDate.now().plusYears(5), LocalDate.now(), client1);
//
//			cardRepository.save(card1);
//			cardRepository.save(card2);
//			cardRepository.save(card3);
		};
	}

}
