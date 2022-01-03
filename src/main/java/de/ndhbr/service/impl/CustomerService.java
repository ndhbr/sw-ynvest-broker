package de.ndhbr.service.impl;

import de.ndhbr.entity.CustomerType;
import de.ndhbr.entity.StockOrder;
import de.ndhbr.entity.Customer;
import de.ndhbr.repository.CustomerRepo;
import de.ndhbr.service.CustomerServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements CustomerServiceIF {

    @Autowired
    private CustomerRepo customerRepo;

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
    public Customer registerCustomer(Customer customer) throws ServiceException {
        Optional<Customer> foundCustomer = customerRepo.findById(customer.getID());

        if (foundCustomer.isEmpty()) {
            customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
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
