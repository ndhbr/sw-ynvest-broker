package de.ndhbr.ynvest.api;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.CustomerServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@Transactional
@RequestMapping(path = "/api")
public class ApiController {

    @Autowired
    OrderServiceIF orderService;

    @Autowired
    BankAccountServiceIF bankAccountService;

    @PutMapping("/order")
    public String completeOrder(@RequestBody StockOrder order) {
        ApiResponse result;

        try {
            orderService.completeOrderById(order.getOrderId());
            result = new ApiResponse(ApiResult.Success,
                    "Der Auftrag wurde erfolgreich abgeschlossen.");
        } catch (ServiceException e) {
            result = new ApiResponse(ApiResult.Error,
                    e.getMessage());
        }

        return result.toJson();
    }

    @PutMapping("/bank-account")
    public String updateBankAccount(@RequestBody BankAccount bankAccount) {
        ApiResponse result;

        try {
            BankAccount foundBankAccount =
                    bankAccountService.getBankAccountByIban(bankAccount.getIban());
            double newVirtualBalance = bankAccount.getBalance() -
                    orderService.getSumOfOpenOrders(foundBankAccount.getCustomer());

            foundBankAccount.setBalance(bankAccount.getBalance());
            foundBankAccount.setVirtualBalance(newVirtualBalance);

            bankAccountService.saveBankAccount(foundBankAccount);
            result = new ApiResponse(ApiResult.Success, "Kontostand wurde aktualisiert.");
        } catch (ServiceException e) {
            result = new ApiResponse(ApiResult.Error,
                    e.getMessage());
        }

        return result.toJson();
    }
}
