package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
//import eBank.DTO.BenutzerDTO;

@Component
public class BankClient implements BankClientIF {

    @Autowired @Qualifier("bank")
    WebClient webClient;

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount) {

        /*return webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/ueberweisung").build())
                .retrieve()
                .toEntity();*/
        return null;
    }
}
