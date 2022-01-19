package de.ndhbr.ynvest.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("singleton")
public class FailureController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(ModelMap model) {
        model.addAttribute("content", "error");
        return "index";
    }
}
