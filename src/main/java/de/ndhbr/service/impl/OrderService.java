package de.ndhbr.service.impl;

import de.ndhbr.entity.StockOrder;
import de.ndhbr.repository.OrderRepo;
import de.ndhbr.service.OrderServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService implements OrderServiceIF {

    @Autowired
    private OrderRepo orderRepo;

    @Override
    public void updateOrder(StockOrder stockOrder) {

    }

    @Override
    public StockOrder createOrder(StockOrder stockOrder) {
        return orderRepo.save(stockOrder);
    }

    @Override
    public Iterable<StockOrder> getAllOrders() {
        return orderRepo.findAll();
    }
}
