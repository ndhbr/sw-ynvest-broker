package de.ndhbr.entity;

import javax.persistence.Embeddable;

@Embeddable
public class Wertpapier {
    private String isin;
    private int menge;
}
