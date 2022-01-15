package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.entity.Portfolio;
import de.ndhbr.ynvest.repository.PortfolioRepo;
import de.ndhbr.ynvest.service.PortfolioServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Scope("singleton")
public class PortfolioService implements PortfolioServiceIF {

    @Autowired
    PortfolioRepo portfolioRepo;

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepo.save(portfolio);
    }
}
