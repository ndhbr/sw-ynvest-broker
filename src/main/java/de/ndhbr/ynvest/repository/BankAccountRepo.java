package de.ndhbr.ynvest.repository;

import de.ndhbr.ynvest.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, String> { }
