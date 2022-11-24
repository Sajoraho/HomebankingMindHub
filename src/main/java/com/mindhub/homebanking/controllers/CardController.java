package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
//Controlador, escucha y responde peticiones
@RestController
@RequestMapping("/api") //Asocia una petición con la ruta
public class CardController {

    @Autowired //Inyección de dependencia en el controlador / genera una instancia de cardservice
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @PostMapping("/clients/current/cards") //Asocia una petición con la ruta y cuando le pega al endpoint se ejecuta el metodo debajo
    public ResponseEntity<?> createCard(Authentication authentication, //Contiene los datos del cliente autenticado, email, password y sessionid
                                        @RequestParam CardType cardType, //Parametro de petición, recibe un parametro
                                        @RequestParam CardColor cardColor){
        Client clientCurrent = clientService.findByEmail(authentication.getName()); //Traigo el cliente según el email
        //stream, es un tipo de colección que permite usar funciones de orden superior
        Set<Card> cards = clientCurrent.getCard().stream().filter(card -> card.getType() == cardType).collect(Collectors.toSet());

//        if(cardType.equals(null)){
//            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
//        }

        if(cards.size() >= 3) {
            return new ResponseEntity<>("You cannot have more than 3 " + cardType + " cards" , HttpStatus.FORBIDDEN);
        }

        Integer cvv = CardUtils.getCVV();
        String number = CardUtils.getCardNumber();

        Card card = new Card(clientCurrent.nombreCompleto(),
                cardType,
                cardColor,
                number,
                cvv,
                LocalDate.now().plusYears(5),
                LocalDate.now(),
                clientCurrent);

        cardService.saveCard(card);

        return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/clients/current/cards/delete/{id}")
    public ResponseEntity<?> deleteCard(Authentication authentication,
                                        @PathVariable Long id){
        Client clientCurrent = clientService.findByEmail(authentication.getName());
        Set<Card> cardCurrent = clientCurrent.getCard();

        if(clientCurrent == null){
            return new ResponseEntity<>("Client not found", HttpStatus.FORBIDDEN);
        }
        if(id == 0 || id == null){
            return new ResponseEntity<>("Invalid id", HttpStatus.FORBIDDEN);
        }

        Card deletedCard = cardService.findById(id);

        if(deletedCard == null){
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }

        if(!cardCurrent.contains(deletedCard)){
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }

        deletedCard.setEnabled(false);
        cardService.saveCard(deletedCard);

        return new ResponseEntity<>("Card deleted", HttpStatus.OK);
    }

}
