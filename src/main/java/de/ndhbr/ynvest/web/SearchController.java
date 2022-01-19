package de.ndhbr.ynvest.web;

import de.ndhbr.ynvest.api.client.StockExchangeClientIF;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.othr.sw.yetra.entity.Share;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Scope("singleton")
public class SearchController {

    @Autowired
    StockExchangeClientIF stockExchange;

    @RequestMapping("/search")
    public String search(@RequestParam String query, ModelMap model) {
        String searchQuery = (query != null) ? query : "";
        List<Share> results;

        try {
            results = stockExchange.findSharesByKeyword(query);
            model.addAttribute("resultList", results);
        } catch (ServiceUnavailableException | ServiceException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("query", searchQuery);
        model.addAttribute("content", "search");
        return "index";
    }
}
