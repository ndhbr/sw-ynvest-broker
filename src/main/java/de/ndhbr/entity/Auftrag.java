package de.ndhbr.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Auftrag {
    @Id
    private long auftragsNr;
    @Enumerated(EnumType.ORDINAL)
    private AuftragsTyp typ;
    @Enumerated(EnumType.ORDINAL)
    private AuftragsStatus status;
    private String isin;
    private int menge;
    private double stueckPreis;
    private String ereignisUrl;
    private Date platziertAm;
}
