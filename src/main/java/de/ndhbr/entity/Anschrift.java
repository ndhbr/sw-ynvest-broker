package de.ndhbr.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Anschrift {
    private String strasse;
    private String hausnummer;
    private String adresszusatz;
    private String ort;
    private String postleitzahl;
    private String land;
}
