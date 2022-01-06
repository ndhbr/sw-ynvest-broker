package de.ndhbr.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;

@Controller
public class SearchController {

    @RequestMapping("/search")
    public String search(@RequestParam String query, Locale locale, ModelMap model) {
        String searchQuery = (query != null) ? query : "";

        model.addAttribute("query", searchQuery);
        model.addAttribute("content", "search");
        return "index";
    }
}
