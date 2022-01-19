package de.ndhbr.ynvest.web;

import de.ndhbr.ynvest.entity.*;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.websocket.server.PathParam;

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
    public String orders(ModelMap model,
                         @AuthenticationPrincipal Customer customer,
                         @PathParam("page") Integer page) {
        handleOrderList(model, customer, page);

        model.addAttribute("stockOrder", new StockOrder());
        model.addAttribute("content", "orders");
        return "index";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @Transactional
    public String orderAction(@ModelAttribute("stockOrder") StockOrder stockOrder,
                              ModelMap model,
                              @AuthenticationPrincipal Customer customer,
                              @PathParam("page") Integer page) {
        try {
            // Customer
            customer = customerService.getCustomerByEmail(customer.getEmail());

            // Create order
            StockOrder addedOrder = orderService.createOrder(stockOrder, customer);
            customerService.addOrder(addedOrder, customer);

            model.addAttribute("success", "Der Auftrag wurde erfolgreich erstellt!");
        } catch (ServiceUnavailableException | ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }

        handleOrderList(model, customer, page);
        model.addAttribute("content", "orders");
        return "index";
    }

    private void handleOrderList(ModelMap model,
                                 @AuthenticationPrincipal Customer customer,
                                 @PathParam("page") Integer page) {
        if (page == null) {
            page = 1;
        }

        Page<StockOrder> orderList = orderService.getOrders(customer, page - 1);

        model.addAttribute("totalPages", orderList.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("orders", orderList.toList());
    }
}
