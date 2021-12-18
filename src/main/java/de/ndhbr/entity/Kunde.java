package de.ndhbr.entity;

import de.ndhbr.entity.util.SingleIdEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
public class Kunde extends SingleIdEntity<Long> {
    @Id
    private long kundenNr;
    private String name;
    private String passwort;
    private Date registriertAm;
    @Embedded
    private Anschrift anschrift;
    @OneToOne
    private Konto konto;
    @OneToOne
    private Depot depot;
    @OneToMany
    private List<Auftrag> auftraege;

    @Override
    public Long getID() {
        return kundenNr;
    }
}
