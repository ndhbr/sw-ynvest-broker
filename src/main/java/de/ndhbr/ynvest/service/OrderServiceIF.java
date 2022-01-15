package de.ndhbr.ynvest.service;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import org.hibernate.service.spi.ServiceException;

import java.util.List;
import java.util.Optional;

public interface OrderServiceIF {
    Optional<StockOrder> findOrderById(Long orderId);
    StockOrder completeOrderById(Long orderId) throws ServiceException;
    StockOrder createOrder(StockOrder stockOrder, Customer customer);
    List<StockOrder> getOpenOrdersByIsin(Customer customer, String isin);
    double getSumOfOpenOrders(Customer customer);
    double getQuantityOfAllOpenSellOrders(Customer customer);
    Iterable<StockOrder> getAllOrders();
}
