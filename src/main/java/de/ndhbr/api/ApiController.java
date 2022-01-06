package de.ndhbr.api;

import de.ndhbr.entity.StockOrder;
import de.ndhbr.service.OrderServiceIF;
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
}
