package de.ndhbr.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Konto {
    @Id
    private String iban;
    private String nutzername;
    private String passwort;
    private String ereignisUrl;
    private double kontostand;
    private double virtuellerKontostand;
}
