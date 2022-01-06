package de.ndhbr.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
public class FailureController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(Locale locale, ModelMap model) {
        model.addAttribute("content", "error");
        return "index";
    }
}