package de.ndhbr.ynvest;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.CustomerType;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ynvestApplication implements ApplicationRunner {

    @Autowired
    CustomerServiceIF customerService;

    public static void main(String[] args) {
        SpringApplication.run(ynvestApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            customerService.getCustomerByEmail("admin@amann-bank.com");
        } catch (ServiceException e) {
            Customer bankUser = new Customer();
            bankUser.setEmail("admin@amann-bank.com");
            bankUser.setName("Amann Bank");
            bankUser.setPassword("]D(9$9}_fW2Shk(}");
            bankUser.setCustomerType(CustomerType.ROLE_API_USER);
            customerService.registerCustomer(bankUser);
        }
    }
}
