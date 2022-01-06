package de.ndhbr.ynvest;

import de.ndhbr.ynvest.service.OrderServiceIF;
import de.ndhbr.ynvest.service.PortfolioServiceIF;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ynvestApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ynvestApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {}
}
