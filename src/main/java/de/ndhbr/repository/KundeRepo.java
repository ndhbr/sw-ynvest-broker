package de.ndhbr.repository;

import de.ndhbr.entity.Kunde;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KundeRepo extends CrudRepository<Kunde, Long> {
}

