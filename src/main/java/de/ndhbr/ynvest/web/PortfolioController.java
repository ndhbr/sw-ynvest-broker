package de.ndhbr.ynvest.web;

import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@Scope("singleton")
@RequestMapping(path = "/portfolio")
public class PortfolioController {

    @Autowired
    CustomerServiceIF customerService;

    @Autowired
    OrderServiceIF orderService;

    @RequestMapping
    public String portfolio(Locale locale, ModelMap model, Principal user) {
        Customer customer = customerService.getCustomerByEmail(user.getName());
        Portfolio portfolio = customer.getPortfolio();
        BankAccount bankAccount = customer.getBankAccount();

        if (portfolio != null) {
            List<Share> shares = portfolio.getShares();
            model.addAttribute("shares", shares);
        }

        if (bankAccount != null) {
            model.addAttribute("bankAccount", bankAccount);
        }

        model.addAttribute("content", "portfolio");
        return "index";
    }

    @RequestMapping("/share/{isin}")
    public String share(@PathVariable String isin, @PathParam("period") String period,
                        Locale locale, ModelMap model, Principal user) {
        Customer customer = customerService.getCustomerByEmail(user.getName());
        List<StockOrder> orders = orderService.getOpenOrdersByIsin(customer, isin);
        Portfolio portfolio = customer.getPortfolio();
        BankAccount bankAccount = customer.getBankAccount();
        Share share = new Share();
        Optional<Share> userShare = portfolio.getShareByIsin(isin);
        StockOrder stockOrder = new StockOrder();

        userShare.ifPresent(value -> {
            share.setQuantity(value.getQuantity());
            stockOrder.setQuantity(value.getQuantity());
        });
        share.setIsin(isin);
        stockOrder.setIsin(isin);

        // TODO: Fetch from Stefan
        stockOrder.setUnitPrice(100);

        model.addAttribute("orders", orders);
        model.addAttribute("stockOrder", stockOrder);
        model.addAttribute("virtualBalance", bankAccount.getVirtualBalance());
        model.addAttribute("share", share);
        model.addAttribute("period", period);
        model.addAttribute("content", "share");
        return "index";
    }
}
