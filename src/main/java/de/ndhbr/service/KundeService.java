package de.ndhbr.service;

import de.ndhbr.entity.Kunde;

public interface KundeService {
    Kunde getKundeByKundenNr(long kundenNr);
    Kunde registriereKunde(Kunde kunde);
}
