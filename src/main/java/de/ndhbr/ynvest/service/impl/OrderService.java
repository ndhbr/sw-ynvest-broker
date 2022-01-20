package de.ndhbr.ynvest.service.impl;

import de.ndhbr.ynvest.api.client.BankClientIF;
import de.ndhbr.ynvest.api.client.StockExchangeClientIF;
import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.enumeration.OrderType;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.ndhbr.ynvest.repository.OrderRepo;
import de.ndhbr.ynvest.service.OrderServiceIF;
import de.ndhbr.ynvest.util.Constants;
import de.othr.sw.yetra.dto.OrderDTO;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Logger;

@Service
@Scope("singleton")
public class OrderService implements OrderServiceIF {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private StockExchangeClientIF stockExchange;

    @Autowired
    private BankClientIF bankClient;

    @Autowired
    Logger logger;

    @Override
    public Optional<StockOrder> findOrderById(Long orderId) {
        return orderRepo.findById(orderId);
    }

    @Override
    public Page<StockOrder> getOrders(Customer customer, int page) {
        return orderRepo.findStockOrderByCustomer(customer, PageRequest.of(
                page, 5, Sort.by("placedOn").descending()
        ));
    }

    @Override
    public StockOrder saveOrder(StockOrder stockOrder) {
        return orderRepo.save(stockOrder);
    }

    @Override
    @JmsListener(destination = Constants.ORDER_QUEUE)
    public void receiveOrderUpdate(OrderDTO orderDTO) {
        StockOrder order;

        if (orderDTO != null) {
            Optional<StockOrder> optionalOrder = findOrderById(orderDTO.getId());

            if (optionalOrder.isPresent()) {
                order = optionalOrder.get();
                order.mergeWith(orderDTO);

                saveOrder(order);

                try {
                    completeOrderById(order.getOrderId());
                } catch (ServiceUnavailableException | ServiceException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    @Override
    @Transactional
    public void completeOrderById(Long orderId) throws ServiceUnavailableException, ServiceException {
        Optional<StockOrder> foundOrder = findOrderById(orderId);

        if (foundOrder.isPresent()) {
            StockOrder order = foundOrder.get();
            Customer customer = order.getCustomer();
            BankAccount bankAccount = customer.getBankAccount();
            Portfolio portfolio = customer.getPortfolio();
            Optional<Share> foundShare = portfolio
                    .getShareByIsin(order.getIsin());
            Share share;

            if (foundShare.isPresent()) {
                share = foundShare.get();

                switch (order.getType()) {
                    case Buy -> {
                        share.calculateNewPurchasePrice(order.getUnitPrice(), order.getQuantity());
                        share.increaseQuantity(order.getQuantity());
                    }
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
                share.setPurchasePrice(order.getUnitPrice());

                portfolio.insertShare(share);
            }

            bankClient.createTransfer(
                    bankAccount.getIban(),
                    Constants.YNVEST_IBAN,
                    0.99,
                    "ynvest - Order Fee #" + order.getOrderId()
            );

            if (order.getType() == OrderType.Buy) {
                bankClient.createTransfer(
                        bankAccount.getIban(),
                        Constants.YNVEST_IBAN,
                        order.getQuantity() * order.getUnitPrice(),
                        "ynvest - Buy Order #" + order.getOrderId()
                );
            } else {
                bankClient.createTransfer(
                        Constants.YNVEST_IBAN,
                        bankAccount.getIban(),
                        order.getQuantity() * order.getUnitPrice(),
                        "ynvest - Sell Order #" + order.getOrderId()
                );
            }

            portfolioService.savePortfolio(portfolio);
        } else {
            throw new ServiceException("Es wurde kein Auftrag mit der ID " + orderId + " gefunden");
        }
    }

    @Override
    @Transactional
    public StockOrder createOrder(StockOrder stockOrder, Customer customer) throws ServiceUnavailableException,
            ServiceException {
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

        stockOrder.setCustomer(customer);

        double stockPrice = stockExchange.getSharePrice(stockOrder.getIsin());
        if (stockOrder.getUnitPrice() > stockPrice * 1.05 ||
                stockOrder.getUnitPrice() < stockPrice * 0.95) {
            throw new ServiceException("Der Auftragspreis hat sich in der Zwischenzeit verändert. Versuche es erneut.");
        }

        // create order in stock exchange yetra
        stockOrder = stockExchange.createOrder(stockOrder);

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
}
