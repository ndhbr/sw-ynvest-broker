package de.ndhbr.web;

import de.ndhbr.entity.Customer;
import de.ndhbr.entity.Portfolio;
import de.ndhbr.entity.Share;
import de.ndhbr.service.CustomerServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Locale;

@Controller
public class PortfolioController {

    @Autowired
    CustomerServiceIF customerService;

    @RequestMapping("/portfolio")
    public String portfolio(Locale locale, ModelMap model, Principal user) {
        Customer customer = customerService.getCustomerByEmail(user.getName());
        Portfolio portfolio = customer.getPortfolio();

        if (portfolio != null) {
            List<Share> shares = portfolio.getShares();
            model.addAttribute("shares", shares);
        }

        model.addAttribute("content", "portfolio");
        return "index";
    }

    @RequestMapping("/performance")
    public String performance(Locale locale, ModelMap model) {
        model.addAttribute("content", "performance");
        return "index";
    }
}
