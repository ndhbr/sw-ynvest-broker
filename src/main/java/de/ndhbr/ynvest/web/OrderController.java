package de.ndhbr.ynvest.web;

import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Locale;

@Controller
public class OrderController {

    @Autowired
    OrderServiceIF orderService;

    @Autowired
    CustomerServiceIF customerService;

    @Autowired
    BankAccountServiceIF bankAccountService;

    @RequestMapping("/orders")
    public String orders(Locale locale, ModelMap model, Principal user) {
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        Customer customer = customerService.getCustomerByEmail(userId);

        model.addAttribute("orders", customer.getOrders());
        model.addAttribute("stockOrder", new StockOrder());
        model.addAttribute("content", "orders");
        return "index";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @Transactional
    public String orderAction(@ModelAttribute("stockOrder") StockOrder stockOrder,
                              Locale locale, ModelMap model, Principal user) {
        String userId = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        Customer customer = null;

        try {
            customer = customerService.getCustomerByEmail(userId);
            Portfolio portfolio = customer.getPortfolio();
            BankAccount bankAccount = customer.getBankAccount();

            // TODO: negative shares should not be possible
            if ((stockOrder.getQuantity() < 0 || (stockOrder.getType() != null && stockOrder.getType() == OrderType.Sell)) &&
                    portfolio.getShareQuantity(stockOrder.getIsin()) < Math.abs(stockOrder.getQuantity())) {
                throw new ServiceException("Du besitzt leider nicht genügend Anteile dieser Firma.");
            }

            if (bankAccount.getVirtualBalance() < (stockOrder.getUnitPrice() * stockOrder.getQuantity())) {
                throw new ServiceException("Du hast leider nicht genug Geld auf deinem Guthaben.");
            }

            // Create order
            StockOrder addedOrder = orderService.createOrder(stockOrder, customer);
            customerService.addOrder(addedOrder, customer);

            // Remove virtual money
            if (addedOrder.getType().equals(OrderType.Buy)) {
                bankAccountService.handleNewBuyOrder(bankAccount,
                        addedOrder.getQuantity() * addedOrder.getUnitPrice());
            } else {
                bankAccountService.handleNewSellOrder(bankAccount,
                        addedOrder.getQuantity() * addedOrder.getUnitPrice());
            }
        } catch (ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }

        if (customer != null) {
            model.addAttribute("orders", customer.getOrders());
        }
        model.addAttribute("content", "orders");
        return "index";
    }
}
