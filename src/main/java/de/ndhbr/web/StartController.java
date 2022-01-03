package de.ndhbr.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class StartController {

    @RequestMapping("/")
    public String root(Locale locale, ModelMap model) {
        model.addAttribute("content", "start");
        return "index";
    }

    @RequestMapping("/error")
    public String error(Locale locale, ModelMap model) {
        model.addAttribute("content", "error");
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
