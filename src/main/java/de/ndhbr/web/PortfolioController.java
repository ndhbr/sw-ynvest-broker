package de.ndhbr.web;

import de.ndhbr.entity.*;
import de.ndhbr.service.CustomerServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping(path = "/portfolio")
public class PortfolioController {

    @Autowired
    CustomerServiceIF customerService;

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

    @RequestMapping("/share")
    public String share(@RequestParam String isin, Locale locale, ModelMap model, Principal user) {
        Customer customer = customerService.getCustomerByEmail(user.getName());
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

        model.addAttribute("stockOrder", stockOrder);
        model.addAttribute("virtualBalance", bankAccount.getVirtualBalance());
        model.addAttribute("share", share);
        model.addAttribute("content", "share");
        return "index";
    }

    @RequestMapping("/performance")
    public String performance(Locale locale, ModelMap model) {
        model.addAttribute("content", "performance");
        return "index";
    }
}
