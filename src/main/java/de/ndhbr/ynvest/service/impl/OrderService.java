package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.repository.OrderRepo;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Logger;

import static java.util.UUID.randomUUID;

@Service
@Scope("singleton")
public class OrderService implements OrderServiceIF {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private Logger logger;

    @Override
    public Optional<StockOrder> findOrderById(Long orderId) {
        return orderRepo.findById(orderId);
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

                if (share.getQuantity() > 0) {
                    portfolio.updateShare(share);
                } else {
                    portfolio.removeShare(share);
                }
            } else {
                share = new Share();
                share.setIsin(order.getIsin());
                share.setQuantity(order.getQuantity());

                portfolio.insertShare(share);
            }

            // TODO: SCHNITTSTELLE ZU SINA BANK
            //  Call Amann Bank to transfer money
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
        Portfolio portfolio = customer.getPortfolio();

        // Check for open sell orders
        if (stockOrder.getType() == OrderType.Sell &&
                (portfolio.getShareQuantity(stockOrder.getIsin()) -
                        getQuantityOfAllOpenSellOrders(customer)) <
                        stockOrder.getQuantity()) {
            throw new ServiceException("Du kannst keine Anteile mehr" +
                    " verkaufen, da du bereits zu viele Verkaufsaufträge" +
                    " zu dieser Aktie hast.");
        }

        stockOrder.setOrderId(randomUUID().getMostSignificantBits() & Long.MAX_VALUE); // TODO: Remove
        stockOrder.setCustomer(customer);
        stockOrder.setPlacedOn(new Date());

        // TODO: SCHNITTSTELLE STEFAN - Ask for current price
        // AND SEND IT TO HIM
        if (stockOrder.getUnitPrice() != 100.0) {
            throw new ServiceException("Der Auftragspreis hat sich in der Zwischenzeit verändert. Versuche es erneut.");
        }

        stockOrder.setStatus(OrderStatus.Open);

        return orderRepo.save(stockOrder);
    }


    @Override
    public List<StockOrder> getOpenOrdersByIsin(Customer customer, String isin) {
        return orderRepo.getOpenOrdersByIsin(customer, isin);
    }

    @Override
    public double getSumOfOpenOrders(Customer customer) {
        return orderRepo.getSumOfAllOpenOrdersByCustomer(customer);
    }

    @Override
    public double getQuantityOfAllOpenSellOrders(Customer customer) {
        return orderRepo.getQuantityOfAllOpenSellOrdersByCustomer(customer);
    }

    @Override
    public Iterable<StockOrder> getAllOrders() {
        return orderRepo.findAll();
    }

    public Iterable<StockOrder> getOrdersByParent() {
        return orderRepo.findAll(
                PageRequest.of(0, 6, Sort.by("placedOn").descending())
        );
    }
}
