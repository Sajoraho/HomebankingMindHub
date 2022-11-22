package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

//Interfaz, permite la conexión de dos objetos
@RepositoryRestResource //Se está indicando que se va a trabajar con las restricciones de Rest; formato Json o XML y con las peticiones HTTP en el estado actual
public interface ClientRepository extends JpaRepository<Client, Long> { //Repository, extrae y lleva datos con las bd
    Client findByEmail(String email);                   //Indico el tipo de dato con el que se va a trabajar//Objeto de tipo Client

    //Jpa, persite la api en la bd
}
