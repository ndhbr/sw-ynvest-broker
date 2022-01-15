package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.repository.CustomerRepo;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import de.ndhbr.ynvest.service.PortfolioServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Scope("singleton")
public class CustomerService implements CustomerServiceIF {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private PortfolioServiceIF portfolioService;

    @Autowired
    private BankAccountServiceIF bankAccountService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepo.findById(email).orElseThrow(
                () -> new ServiceException("Keinen Kunde mit der E-Mail Adresse " + email
                        + "gefunden")
        );
    }

    @Override
    @Transactional
    public Customer registerCustomer(Customer customer) throws ServiceException {
        Optional<Customer> foundCustomer = customerRepo.findById(customer.getID());

        if (foundCustomer.isEmpty()) {
            if (customer.getCustomerType() != CustomerType.ROLE_API_USER) {
                // Create portfolio
                Portfolio portfolio = portfolioService.savePortfolio(new Portfolio());

                // Create bank account
                // TODO: SCHNITTSTELLE ZU SINA BANK!
                BankAccount bankAccount = new BankAccount();
                bankAccount.setRandomIbanTmp(); // TODO: Replace with account creation in Amann Bank
                bankAccount.setBalance(10000);
                bankAccount.setVirtualBalance(10000);
                bankAccountService.saveBankAccount(bankAccount);

                customer.setPortfolio(portfolio);
                customer.setBankAccount(bankAccount);
            }

            customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
            customer.setRegisteredOn(new Date());

            return customerRepo.save(customer);
        }

        throw new ServiceException("Ein Benutzer mit dieser E-Mail Adresse existiert bereits!");
    }

    @Override
    public Customer verifyCustomer(Customer customer) {
        customer.setCustomerType(CustomerType.ROLE_VERIFIED);
        return customerRepo.save(customer);
    }

    @Override
    @Transactional
    public void addOrder(StockOrder stockOrder, Customer customer) {
        if (customer != null) {
            BankAccount bankAccount = customer.getBankAccount();
            Portfolio portfolio = customer.getPortfolio();

            // If has enough shares
            if ((stockOrder.getType() != null && stockOrder.getType() == OrderType.Sell) &&
                    portfolio.getShareQuantity(stockOrder.getIsin()) < Math.abs(stockOrder.getQuantity())) {
                throw new ServiceException("Du besitzt leider nicht genügend Anteile dieser Firma.");
            }

            // If has enough money
            if (bankAccount.getVirtualBalance() < (stockOrder.getUnitPrice() * stockOrder.getQuantity())) {
                throw new ServiceException("Du hast leider nicht genug Geld auf deinem Guthaben.");
            }

            // Add order to customer
            customer.addOrder(stockOrder);

            // Remove virtual money
            if (stockOrder.getType().equals(OrderType.Buy)) {
                bankAccountService.handleNewBuyOrder(bankAccount,
                        stockOrder.getQuantity() * stockOrder.getUnitPrice());
            } else {
                bankAccountService.handleNewSellOrder(bankAccount,
                        stockOrder.getQuantity() * stockOrder.getUnitPrice());
            }

            customerRepo.save(customer);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepo.findById(username).orElseThrow(
                () -> new UsernameNotFoundException("Keinen Kunde mit der E-Mail Adresse " + username
                        + "gefunden")
        );
    }

    @Override
    public List<StockOrder> getOrders(Customer customer) {
        return customer.getOrders();
    }
}
