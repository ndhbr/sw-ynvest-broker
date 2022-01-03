package de.ndhbr.web;

import de.ndhbr.entity.Customer;
import de.ndhbr.entity.OrderStatus;
import de.ndhbr.entity.OrderType;
import de.ndhbr.entity.StockOrder;
import de.ndhbr.service.OrderServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static java.util.UUID.randomUUID;

@Controller
//@Scope(value = "session")
public class OrderController {

    @Autowired
    OrderServiceIF orderService;

    @RequestMapping("/orders")
    public String orders(Locale locale, ModelMap model, Principal user) {
        Customer customer = (Customer) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        model.addAttribute("orders", customer.getOrders());
        model.addAttribute("stockOrder", new StockOrder());
        model.addAttribute("content", "orders");
        return "index";
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    @Transactional
    public String orderAction(@ModelAttribute("stockOrder") StockOrder stockOrder,
                              Locale locale, ModelMap model, Principal user) {
        Customer customer = (Customer) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        try {
            // TODO: TMP -> Random Stock Price
            Random r = new Random();
            double randomValue = 1.0 + (500.0 - 1.0) * r.nextDouble();

            stockOrder.setOrderId(randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
            stockOrder.setType(OrderType.Buy);
            stockOrder.setUnitPrice(randomValue);
            stockOrder.setStatus(OrderStatus.Open);
            stockOrder.setPlacedOn(new Date());

            StockOrder addedOrder = orderService.createOrder(stockOrder);
            customer.addOrder(addedOrder);
        } catch (Exception e) {
            // TODO!
        }

        System.out.println(stockOrder.getIsin());

        model.addAttribute("orders", customer.getOrders());
        model.addAttribute("content", "orders");
        return "index";
    }
}
