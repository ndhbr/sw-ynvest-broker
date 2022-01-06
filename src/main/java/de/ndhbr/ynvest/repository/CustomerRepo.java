package de.ndhbr.ynvest.repository;

import de.ndhbr.ynvest.entity.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends CrudRepository<Customer, String> {
}

