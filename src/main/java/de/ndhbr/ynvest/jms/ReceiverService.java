package de.ndhbr.ynvest.jms;

import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.service.OrderServiceIF;
import de.ndhbr.ynvest.util.Constants;
import de.othr.sw.yetra.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReceiverService {

    @Autowired
    OrderServiceIF orderService;

    @JmsListener(destination = Constants.ORDER_QUEUE)
    public void receiveMessage(OrderDTO orderDTO) {
        StockOrder order;

        if (orderDTO != null) {
            Optional<StockOrder> optionalOrder = orderService.findOrderById(orderDTO.getId());

            if (optionalOrder.isPresent()) {
                order = optionalOrder.get();
                order.mergeWith(orderDTO);

                orderService.saveOrder(order);
                // TODO: Pass Order Object?
                orderService.completeOrderById(order.getOrderId());
            }
        }
    }
}