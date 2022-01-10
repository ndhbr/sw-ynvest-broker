package de.ndhbr.ynvest.service;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import org.hibernate.service.spi.ServiceException;

import java.util.Optional;

public interface OrderServiceIF {
    Optional<StockOrder> findOrderById(Long orderId);
    void updateOrder(StockOrder stockOrder);
    StockOrder completeOrderById(Long orderId) throws ServiceException;
    StockOrder createOrder(StockOrder stockOrder, Customer customer);
    double getSumOfOpenOrders(Customer customer);
    Iterable<StockOrder> getAllOrders();
}
