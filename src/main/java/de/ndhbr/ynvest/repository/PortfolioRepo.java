package de.ndhbr.ynvest.repository;

import de.ndhbr.ynvest.entity.Portfolio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepo extends CrudRepository<Portfolio, String> { }
