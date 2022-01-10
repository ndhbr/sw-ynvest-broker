package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.repository.OrderRepo;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static java.util.UUID.randomUUID;

@Service
public class OrderService implements OrderServiceIF {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private PortfolioService portfolioService;

    @Override
    public Optional<StockOrder> findOrderById(Long orderId) {
        return orderRepo.findById(orderId);
    }

    @Override
    public void updateOrder(StockOrder stockOrder) {
        // TODO: Implement or remove
    }

    @Override
    @Transactional
    public StockOrder completeOrderById(Long orderId) throws ServiceException {
        Optional<StockOrder> foundOrder = findOrderById(orderId);

        if (foundOrder.isPresent()) {
            StockOrder order = foundOrder.get();
            Customer customer = order.getCustomer();
            Portfolio portfolio = customer.getPortfolio();
            Optional<Share> foundShare = portfolio
                    .getShareByIsin(order.getIsin());
            Share share;

            if (foundShare.isPresent()) {
                share = foundShare.get();

                switch (order.getType()) {
                    case Buy -> share.increaseQuantity(order.getQuantity());
                    case Sell -> share.decreaseQuantity(order.getQuantity());
                }

                portfolio.updateShare(share);
            } else {
                share = new Share();
                share.setIsin(order.getIsin());
                share.setQuantity(order.getQuantity());

                portfolio.insertShare(share);
            }

            // TODO: Call Amann Bank transfer money
            // -> 0,99 ct to my bank account
            // -> Amount to stock exchange (yetra)

            portfolioService.savePortfolio(portfolio);
            order.setStatus(OrderStatus.Completed);

            return orderRepo.save(order);
        } else {
            // TODO: Not found exception
            throw new ServiceException("Es wurde kein Auftrag mit der ID " + orderId + " gefunden");
        }
    }

    @Override
    public StockOrder createOrder(StockOrder stockOrder, Customer customer) {
        stockOrder.setOrderId(randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
        stockOrder.setCustomer(customer);
        stockOrder.setPlacedOn(new Date());

        if (stockOrder.getType() == null) {
            if (stockOrder.getQuantity() < 0) {
                stockOrder.setType(OrderType.Sell);
                stockOrder.setQuantity(Math.abs(stockOrder.getQuantity()));
            } else {
                stockOrder.setType(OrderType.Buy);
            }
        }

        stockOrder.setStatus(OrderStatus.Open);

        return orderRepo.save(stockOrder);
    }

    @Override
    public double getSumOfOpenOrders(Customer customer) {
        return orderRepo.getSumOfAllOpenOrdersByCustomer(customer);
    }

    @Override
    public Iterable<StockOrder> getAllOrders() {
        return orderRepo.findAll();
    }
}
