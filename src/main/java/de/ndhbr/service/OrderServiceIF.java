package de.ndhbr.service;

import de.ndhbr.entity.StockOrder;

public interface OrderServiceIF {
    void updateOrder(StockOrder stockOrder);
    StockOrder createOrder(StockOrder stockOrder);
    Iterable<StockOrder> getAllOrders();
}
