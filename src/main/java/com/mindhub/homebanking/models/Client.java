package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;
//un objeto es una instancia de una clase. Una instancia es un objeto que deriva de otro
@Entity //Crea una tabla en la base de datos
public class Client { //Con la clase represento objetos de la vida real. La Clase es una plantilla para crear objetos
    @Id //Asigno id como primary key
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //Genero un valor para la primary key de forma automatica
    @GenericGenerator(name="native", strategy = "native") //Verifica que el valor generado en el BD no se repita
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    //----------------------------------------------------------------

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans = new HashSet<>();

    //----------------------------------------------------------------

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Card> card = new HashSet<>();

    public Client() { //Se crea un constructor vacio por requerimientos de Spring para que se haga uso al momento de proveer objetos
    }

    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    //Getter y setter, metodos accesores
    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //----------------------------------------------------------------
    public String nombreCompleto(){
        return this.firstName + " " + this.lastName;
    }

    //----------------------------------------------------------------
    public Set<Account> getAccounts() {
        return accounts;
    }

    //----------------------------------------------------------------
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    @JsonIgnore
    public List<Loan> getLoans() {
        return clientLoans.stream().map(clientLoans -> clientLoans.getLoan()).collect(toList());
    }

    //----------------------------------------------------------------
    public Set<Card> getCard() {
        return card;
    }

    //----------------------------------------------------------------
    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
