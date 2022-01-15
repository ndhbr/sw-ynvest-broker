package de.ndhbr.ynvest.api;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Scope("singleton")
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

    @PatchMapping("/bankAccounts/{iban}")
    @Transactional
    public String updateBankAccount(@PathVariable String iban,
                                    @RequestBody BankAccount bankAccount) {
        ApiResponse result;

        try {
            BankAccount foundBankAccount =
                    bankAccountService.getBankAccountByIban(iban);
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
