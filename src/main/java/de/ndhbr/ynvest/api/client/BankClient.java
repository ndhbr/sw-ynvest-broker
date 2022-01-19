package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.Address;
import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.ndhbr.ynvest.util.Constants;
import eBank.DTO.BenutzerDTO;
import eBank.DTO.KontoDTO;
import eBank.DTO.UeberweisungDTO;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
public class BankClient implements BankClientIF {

    private final String bankNotAvailable = "Bank eBank nicht " +
            "erreichbar oder fehlerhaft.";

    @Autowired
    @Qualifier("bank")
    WebClient webClient;

    @Autowired
    Logger logger;

    @Override
    public void createTransfer(String ibanSender, String ibanReceiver, double amount,
                               String purposeOfUse) throws ServiceUnavailableException,
            ServiceException {
        ResponseEntity<Void> response;
        UeberweisungDTO ueberweisungDTO = new UeberweisungDTO();
        ueberweisungDTO.setIbanSender(ibanSender);
        ueberweisungDTO.setIbanEmpfaenger(ibanReceiver);
        ueberweisungDTO.setBetrag(amount);
        ueberweisungDTO.setKommentar(purposeOfUse);

        System.out.println(ueberweisungDTO.getIbanSender());
        System.out.println(ueberweisungDTO.getIbanEmpfaenger());
        System.out.println(ueberweisungDTO.getBetrag());
        System.out.println(ueberweisungDTO.getKommentar());

        try {
            response = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder.path("/ueberweisung").build())
                    .body(Mono.just(ueberweisungDTO), UeberweisungDTO.class)
                    .retrieve()
                    .toBodilessEntity()
                    .block(Constants.WEBCLIENT_TIMEOUT);

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                logger.info("Überweisung vollständig - Auftrag abgeschlossen");
            } else {
                throw new ServiceException("Bei der Überweisung der eBank ist ein Fehler aufgetreten");
            }
        } catch (WebClientRequestException e) {
            throw new ServiceUnavailableException(bankNotAvailable);
        }
    }

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount, Customer customer)
            throws ServiceUnavailableException {
        ResponseEntity<KontoDTO> response;
        Address address = customer.getAddress();

        BenutzerDTO benutzer = new BenutzerDTO();
        benutzer.setBenachrichtigung(true);
        benutzer.setEmail(customer.getEmail());
        benutzer.setLand(address.getCountry());
        benutzer.setOrt(address.getCity());
        benutzer.setPostleitzahl(address.getPostal());
        benutzer.setStrasse(address.getStreet() + " " + address.getHouseNumber());
        benutzer.setPasswort(bankAccount.getPassword());

        String[] names = customer.getName().split(" ");
        if (names.length > 1) {
            benutzer.setVorname(names[0]);
            benutzer.setNachname(names[1]);
        } else {
            benutzer.setVorname(names[0]);
        }

        try {
            response = webClient
                    .post()
                    .uri(uriBuilder -> uriBuilder.path("/registrieren").build())
                    .body(Mono.just(benutzer), BenutzerDTO.class)
                    .retrieve()
                    .toEntity(KontoDTO.class)
                    .block(Constants.WEBCLIENT_TIMEOUT);

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                KontoDTO konto = response.getBody();

                assert konto != null;

                bankAccount.setIban(konto.getIban());
                bankAccount.setBalance(konto.getKontostand());
                bankAccount.setVirtualBalance(konto.getKontostand());
            } else {
                throw new ServiceException("Bei der Kontoerstellung durch die eBank ist ein Fehler" +
                        "aufgetreten");
            }
        } catch (WebClientRequestException e) {
            throw new ServiceUnavailableException(bankNotAvailable);
        }

        return bankAccount;
    }
}
