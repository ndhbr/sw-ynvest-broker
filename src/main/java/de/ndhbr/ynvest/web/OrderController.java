package de.ndhbr.ynvest.web;

import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.Locale;

@Controller
@Scope("singleton")
public class OrderController {

    @Autowired
    OrderServiceIF orderService;

    @Autowired
    CustomerServiceIF customerService;

    @Autowired
    BankAccountServiceIF bankAccountService;

    @RequestMapping("/orders")
    public String orders(Locale locale, ModelMap model) {
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
            // Customer
            customer = customerService.getCustomerByEmail(userId);

            // Create order
            StockOrder addedOrder = orderService.createOrder(stockOrder, customer);
            customerService.addOrder(addedOrder, customer);

            model.addAttribute("success", "Der Auftrag wurde erfolgreich erstellt!");
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
