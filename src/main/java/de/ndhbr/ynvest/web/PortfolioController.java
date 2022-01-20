package de.ndhbr.ynvest.web;

import de.ndhbr.ynvest.api.client.StockExchangeClientIF;
import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import de.ndhbr.ynvest.util.MathUtils;
import de.othr.sw.yetra.dto.ShareDetailsDTO;
import de.othr.sw.yetra.dto.TimePeriodDTO;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Scope("singleton")
@RequestMapping(path = "/portfolio")
public class PortfolioController {

    @Autowired
    CustomerServiceIF customerService;

    @Autowired
    OrderServiceIF orderService;

    @Autowired
    StockExchangeClientIF stockExchange;

    @RequestMapping
    @Transactional
    public String portfolio(ModelMap model, Principal user) {
        Customer customer = customerService.getCustomerByEmail(user.getName());
        Portfolio portfolio = customer.getPortfolio();
        BankAccount bankAccount = customer.getBankAccount();

        if (portfolio != null) {
            List<Share> shares = portfolio.getShares();
            List<Double> shareDifferences = new ArrayList<>();
            double portfolioValue = 0.0;
            double portfolioValuePurchase = 0.0;
            double difference = 0.0;

            try {
                List<de.othr.sw.yetra.entity.Share> detailedShares = stockExchange.getSharesByIsins(
                        shares.stream().map(Share::getIsin).collect(Collectors.toList())
                );

                if (detailedShares.size() == shares.size()) {
                    for (int i = 0; i < shares.size(); i++) {
                        portfolioValue += detailedShares.get(i).getCurrentPrice() *
                                shares.get(i).getQuantity();
                        portfolioValuePurchase += shares.get(i).getPurchasePrice() * shares.get(i).getQuantity();

                        shareDifferences.add(
                                MathUtils.calculateRoundedDifferenceBetweenTwoValues(
                                        shares.get(i).getPurchasePrice(), detailedShares.get(i).getCurrentPrice()
                                )
                        );
                    }

                    difference = MathUtils.calculateRoundedDifferenceBetweenTwoValues(
                            portfolioValuePurchase, portfolioValue);
                }
                model.addAttribute("shareDetails", detailedShares);
            } catch (ServiceUnavailableException | ServiceException e) {
                model.addAttribute("error", e.getMessage());
            }

            model.addAttribute("shares", portfolio.getShares());
            model.addAttribute("shareDifferences", shareDifferences);
            model.addAttribute("portfolioValue", portfolioValue);
            model.addAttribute("portfolioValueDifference", difference);
        }

        if (bankAccount != null) {
            model.addAttribute("bankAccount", bankAccount);
        }

        model.addAttribute("content", "portfolio");
        return "index";
    }

    @RequestMapping("/share/{isin}")
    @Transactional
    public String share(@PathVariable String isin, @PathParam("period") TimePeriodDTO period,
                        ModelMap model, Principal user) {
        Customer customer = customerService.getCustomerByEmail(user.getName());
        List<StockOrder> orders = orderService.getOpenOrdersByIsin(customer, isin);
        Portfolio portfolio = customer.getPortfolio();
        BankAccount bankAccount = customer.getBankAccount();
        Share share = new Share();
        Optional<Share> userShare = portfolio.getShareByIsin(isin);
        StockOrder stockOrder = new StockOrder();

        if (period == null) {
            period = TimePeriodDTO.DAY;
        }

        // External
        double difference = 0.0;
        try {
            ShareDetailsDTO shareDetails = stockExchange.getShareDetails(isin, period);
            difference = MathUtils.calculateRoundedDifferenceOfMarktValues(
                    shareDetails.getMarketValues());

            stockOrder.setUnitPrice(shareDetails.getCurrentPrice());
            model.addAttribute("shareDetails", shareDetails);
        } catch (ServiceUnavailableException | ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("difference", difference);

        userShare.ifPresent(value -> {
            share.setQuantity(value.getQuantity());
            stockOrder.setQuantity(value.getQuantity());
        });
        share.setIsin(isin);
        stockOrder.setIsin(isin);

        model.addAttribute("orders", orders);
        model.addAttribute("stockOrder", stockOrder);
        model.addAttribute("virtualBalance", bankAccount.getRoundedVirtualBalance());
        model.addAttribute("share", share);
        model.addAttribute("period", period);
        model.addAttribute("content", "share");
        return "index";
    }
}
