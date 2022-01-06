package de.ndhbr.ynvest.api;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
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
    public String completeOrder(@RequestBody Long orderId) {
        ApiResponse result;

        try {
            StockOrder order = orderService.completeOrderById(orderId);
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

            // TODO: update virtual balance according to current state
            double differenceOld = foundBankAccount.getBalance() -
                    bankAccount.getBalance();
            double differenceVirtual = (foundBankAccount.getBalance() -
                    foundBankAccount.getVirtualBalance()) - differenceOld;
            double newVirtualBalance = bankAccount.getBalance() - differenceVirtual;

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
