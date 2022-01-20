package de.ndhbr.ynvest.service;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.othr.sw.yetra.dto.OrderDTO;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderServiceIF {
    Optional<StockOrder> findOrderById(Long orderId);

    Page<StockOrder> getOrders(Customer customer, int page);

    StockOrder saveOrder(StockOrder stockOrder);

    void receiveOrderUpdate(OrderDTO orderDTO);

    void completeOrderById(Long orderId) throws ServiceUnavailableException, ServiceException;

    StockOrder createOrder(StockOrder stockOrder, Customer customer) throws ServiceUnavailableException;

    List<StockOrder> getOpenOrdersByIsin(Customer customer, String isin);

    double getSumOfOpenOrders(Customer customer);

    double getQuantityOfAllOpenSellOrders(Customer customer);
}
