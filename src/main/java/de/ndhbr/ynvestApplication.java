package de.ndhbr;

import de.ndhbr.entity.*;
import de.ndhbr.service.OrderServiceIF;
import de.ndhbr.service.PortfolioServiceIF;
import de.ndhbr.service.CustomerServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ynvestApplication implements ApplicationRunner {

    @Autowired
    private CustomerServiceIF kundeService;

    @Autowired
    private OrderServiceIF auftragService;

    @Autowired
    private PortfolioServiceIF depotService;

    public static void main(String[] args) {
        SpringApplication.run(ynvestApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            kundeService.getCustomerByEmail("test@ndhbr.de");
        } catch (Exception e) {
            Address address = new Address();
            Portfolio portfolio = new Portfolio();
            Share share1 = new Share();

            share1.setIsin("DE0007664039");
            share1.setQuantity(150);
            portfolio.setShares(List.of(share1));

            depotService.createPortfolio(portfolio);

            StockOrder stockOrder = new StockOrder();
            stockOrder.setOrderId(3987429847L);
            stockOrder.setType(OrderType.Buy);
            stockOrder.setStatus(OrderStatus.Open);
            stockOrder.setIsin("DE0005552004");
            stockOrder.setQuantity(20);
            stockOrder.setUnitPrice(56.64);
            stockOrder.setEventUrl("http://example.com");
            stockOrder.setPlacedOn(new Date());

            auftragService.createOrder(stockOrder);

            StockOrder stockOrder2 = new StockOrder();
            stockOrder2.setOrderId(6937229589L);
            stockOrder2.setType(OrderType.Buy);
            stockOrder2.setStatus(OrderStatus.Open);
            stockOrder2.setIsin("DE0006047004");
            stockOrder2.setQuantity(400);
            stockOrder2.setUnitPrice(59.52);
            stockOrder2.setEventUrl("http://example.com");
            stockOrder2.setPlacedOn(new Date());

            auftragService.createOrder(stockOrder2);

            Customer neuerCustomer = new Customer();
            neuerCustomer.setName("Andreas Huber");
            neuerCustomer.setEmail("test@ndhbr.de");
            neuerCustomer.setPassword("123");
            neuerCustomer.setAddress(address);
            neuerCustomer.setCustomerType(CustomerType.ROLE_VERIFIED);
            neuerCustomer.setOrders(Arrays.asList(stockOrder, stockOrder2));
            neuerCustomer.setPortfolio(portfolio);

            kundeService.registerCustomer(neuerCustomer);

            /*
            StockOrder stockOrder3 = new StockOrder();
            stockOrder3.setOrderId(2246259533L);
            stockOrder3.setType(OrderType.Buy);
            stockOrder3.setStatus(OrderStatus.Completed);
            stockOrder3.setIsin("DE0007664039");
            stockOrder3.setQuantity(150);
            stockOrder3.setUnitPrice(177.48);
            stockOrder3.setEventUrl("http://example.com");
            stockOrder3.setPlacedOn(new Date());

            StockOrder order = auftragService.createOrder(stockOrder3);
            kundeService.addOrder(stockOrder3);
            */
        }
    }
}
