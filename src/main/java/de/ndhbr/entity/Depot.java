package de.ndhbr.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class Depot {
    @Id
    private long depotNr;
    @ElementCollection
    private List<Wertpapier> wertpapiere;
}
