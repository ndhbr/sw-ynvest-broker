package de.ndhbr.ynvest.api;

import de.ndhbr.ynvest.entity.BankAccount;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import eBank.DTO.KontoDTO;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@Scope("singleton")
@RequestMapping(path = "/api")
public class ApiController {

    @Autowired
    OrderServiceIF orderService;

    @Autowired
    BankAccountServiceIF bankAccountService;

    @PatchMapping("/bankAccounts/{iban}")
    @Transactional
    public String updateBankAccount(@PathVariable String iban,
                                    @RequestBody KontoDTO konto,
                                    HttpServletResponse response) {
        ApiResponse result;

        try {
            BankAccount foundBankAccount =
                    bankAccountService.getBankAccountByIban(iban);
            double newVirtualBalance = konto.getKontostand() -
                    orderService.getSumOfOpenOrders(foundBankAccount.getCustomer());

            foundBankAccount.setBalance(konto.getKontostand());
            foundBankAccount.setVirtualBalance(newVirtualBalance);

            bankAccountService.saveBankAccount(foundBankAccount);
            response.setStatus(HttpServletResponse.SC_OK);
            result = new ApiResponse(ApiResult.Success, "Kontostand wurde aktualisiert.");
        } catch (ServiceException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            result = new ApiResponse(ApiResult.Error,
                    e.getMessage());
        }

        return result.toJson();
    }
}
