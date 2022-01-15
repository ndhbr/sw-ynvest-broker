package de.ndhbr.ynvest.web;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Locale;

@Controller
@Scope("singleton")
public class StartController {

    @Autowired
    CustomerServiceIF customerService;

    @RequestMapping("/")
    public String root(Locale locale, ModelMap model, Principal user) {
        if (user != null) {
            String email = user.getName();

            try {
                Customer customer = customerService.getCustomerByEmail(email);
                model.addAttribute("customer", customer);
            } catch (ServiceException e) {
                model.addAttribute("error", e.getMessage());
            }
        }

        model.addAttribute("content", "start");
        return "index";
    }

    @RequestMapping("/faq")
    public String faq(Locale locale, ModelMap model) {
        model.addAttribute("content", "faq");
        return "index";
    }

    @RequestMapping("/support")
    public String support(Locale locale, ModelMap model) {
        model.addAttribute("content", "support");
        return "index";
    }

    @RequestMapping("/about")
    public String about(Locale locale, ModelMap model) {
        model.addAttribute("content", "about");
        return "index";
    }
}
