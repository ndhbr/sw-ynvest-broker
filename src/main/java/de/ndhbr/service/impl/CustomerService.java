package de.ndhbr.service.impl;

import de.ndhbr.entity.*;
import de.ndhbr.repository.CustomerRepo;
import de.ndhbr.service.BankAccountServiceIF;
import de.ndhbr.service.CustomerServiceIF;
import de.ndhbr.service.PortfolioServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
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
            // Create portfolio
            Portfolio portfolio = portfolioService.savePortfolio(new Portfolio());

            // Create bank account
            BankAccount bankAccount = new BankAccount();
            // TODO: Replace with account creation in Amann Bank
            bankAccount.setRandomIbanTmp();
            bankAccount.setBalance(10000);
            bankAccount.setVirtualBalance(10000);
            bankAccountService.saveBankAccount(bankAccount);

            customer.setPortfolio(portfolio);
            customer.setBankAccount(bankAccount);
            customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
            customer.setRegisteredOn(new Date());

            return customerRepo.save(customer);
        }

        throw new ServiceException("Ein Benutzer mit dieser E-Mail Adresse existiert bereits!");
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Customer verifyCustomer(Customer customer) {
        customer.setCustomerType(CustomerType.ROLE_VERIFIED);
        return customerRepo.save(customer);
    }

    @Override
    @Transactional
    public void addOrder(StockOrder stockOrder) {
        Customer customer = (Customer) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        if (customer != null) {
            customer.addOrder(stockOrder);
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
