package de.ndhbr.ynvest.service;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomerServiceIF extends UserDetailsService {
    Customer getCustomerByEmail(String email);
    Customer registerCustomer(Customer customer) throws ServiceUnavailableException;
    Customer verifyCustomer(Customer customer) throws ServiceUnavailableException;
    void addOrder(StockOrder stockOrder, Customer customer);
    List<StockOrder> getOrders(Customer customer);
}
