package de.ndhbr.ynvest.service;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import org.hibernate.service.spi.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderServiceIF {
    Optional<StockOrder> findOrderById(Long orderId);

    StockOrder saveOrder(StockOrder stockOrder);

    void completeOrderById(Long orderId) throws ServiceUnavailableException, ServiceException;

    StockOrder createOrder(StockOrder stockOrder, Customer customer) throws ServiceUnavailableException;

    List<StockOrder> getOpenOrdersByIsin(Customer customer, String isin);

    double getSumOfOpenOrders(Customer customer);

    double getQuantityOfAllOpenSellOrders(Customer customer);

    Iterable<StockOrder> getAllOrders();
}
