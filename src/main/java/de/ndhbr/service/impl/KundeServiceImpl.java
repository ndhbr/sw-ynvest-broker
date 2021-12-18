package de.ndhbr.service.impl;

import de.ndhbr.entity.Kunde;
import de.ndhbr.repository.KundeRepo;
import de.ndhbr.service.KundeService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;

public class KundeServiceImpl implements KundeService {

    @Autowired
    private KundeRepo kundeRepo;

    @Override
    public Kunde getKundeByKundenNr(long kundenNr) {
        return kundeRepo.findById(kundenNr).orElseThrow(
                () -> new ServiceException("Keinen Kunde mit der " + kundenNr
                        + "gefunden")
        );
    }

    @Override
    public Kunde registriereKunde(Kunde kunde) {
        return null;
    }
}
