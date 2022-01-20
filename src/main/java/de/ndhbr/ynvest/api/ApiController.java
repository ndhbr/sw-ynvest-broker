package de.ndhbr.ynvest.api;

import de.ndhbr.ynvest.dto.ApiResponseDTO;
import de.ndhbr.ynvest.enumeration.ApiResult;
import de.ndhbr.ynvest.service.BankAccountServiceIF;
import de.ndhbr.ynvest.service.OrderServiceIF;
import eBank.DTO.KontoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
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
    public ApiResponseDTO updateBankAccount(@PathVariable String iban,
                                            @RequestBody KontoDTO konto,
                                            HttpServletResponse response) {
        bankAccountService.updateBankAccountBalance(iban, konto);

        response.setStatus(HttpStatus.OK.value());
        return new ApiResponseDTO(ApiResult.Success, "Kontostand wurde aktualisiert.");
    }
}
