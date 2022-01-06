package de.ndhbr.service;

import de.ndhbr.entity.StockOrder;
import org.hibernate.service.spi.ServiceException;

import java.util.Optional;

public interface OrderServiceIF {
    Optional<StockOrder> findOrderById(Long orderId);
    void updateOrder(StockOrder stockOrder);
    StockOrder completeOrderById(Long orderId) throws ServiceException;
    StockOrder createOrder(StockOrder stockOrder);
    Iterable<StockOrder> getAllOrders();
}
