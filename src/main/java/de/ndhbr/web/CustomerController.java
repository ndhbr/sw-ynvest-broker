package de.ndhbr.web;

import de.ndhbr.entity.Address;
import de.ndhbr.entity.Customer;
import de.ndhbr.service.CustomerServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Locale;

@Controller
public class CustomerController {

    @Autowired
    CustomerServiceIF customerService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Locale locale, ModelMap model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("content", "login");
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginAction(@ModelAttribute("customer") Customer customer,
                              ModelMap model) {
        model.addAttribute("content", "start");
        return "index";
    }

    @RequestMapping("/register")
    public String register(Locale locale, ModelMap model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("content", "register");
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerAction(@ModelAttribute("customer") Customer customer,
                                 HttpServletRequest request,
                                 Locale locale, ModelMap model) {
        try {
            customerService.registerCustomer(customer);

            model.addAttribute("success", "Erfolgreich registriert! " +
                    "Du kannst dich nun einloggen.");
            model.addAttribute("content", "login");
        } catch (ServiceException e) {
            // Via HTML auf model zugreifen, um responsive zu gestalten
            model.addAttribute("error", e.getMessage());
            model.addAttribute("content", "register");
        }

        return "index";
    }

    @RequestMapping("/verify")
    public String verify(Locale locale, ModelMap model) {
        model.addAttribute("address", new Address());
        model.addAttribute("content", "verify");
        return "index";
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    @Transactional
    public String verifyAction(@ModelAttribute("address") Address address,
                               HttpServletRequest request,
                               Locale locale, ModelMap model,
                               Principal user) {
        try {
            Customer customer = (Customer) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();

            customer.setAddress(address);
            customerService.verifyCustomer(customer);

            model.addAttribute("customer", customer);
        } catch (Exception e) {
            // TODO: ERROR display
            // Via HTML auf model zugreifen, um responsive zu gestalten
            model.addAttribute("error", e.getMessage());
            model.addAttribute("content", "register");
        }

        model.addAttribute("success", "Du hast dich erfolgreich verifiziert!");
        model.addAttribute("content", "start");

        return "index";
    }
}
