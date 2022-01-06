package de.ndhbr.service.impl;

import de.ndhbr.entity.Portfolio;
import de.ndhbr.repository.PortfolioRepo;
import de.ndhbr.service.PortfolioServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService implements PortfolioServiceIF {

    @Autowired
    PortfolioRepo portfolioRepo;

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepo.save(portfolio);
    }
}
