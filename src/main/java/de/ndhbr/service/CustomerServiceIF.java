package de.ndhbr.service;

import de.ndhbr.entity.Customer;
import de.ndhbr.entity.StockOrder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CustomerServiceIF extends UserDetailsService {
    Customer getCustomerByEmail(String email);
    Customer registerCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Customer verifyCustomer(Customer customer);
    void addOrder(StockOrder stockOrder);
    List<StockOrder> getOrders(Customer customer);
}
