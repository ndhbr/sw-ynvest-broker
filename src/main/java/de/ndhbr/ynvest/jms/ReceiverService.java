package de.ndhbr.ynvest.jms;

import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.ndhbr.ynvest.service.OrderServiceIF;
import de.ndhbr.ynvest.util.Constants;
import de.othr.sw.yetra.dto.OrderDTO;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class ReceiverService {

    @Autowired
    OrderServiceIF orderService;

    @Autowired
    Logger logger;

    @JmsListener(destination = Constants.ORDER_QUEUE)
    public void receiveMessage(OrderDTO orderDTO) {
        StockOrder order;

        if (orderDTO != null) {
            Optional<StockOrder> optionalOrder = orderService.findOrderById(orderDTO.getId());

            if (optionalOrder.isPresent()) {
                order = optionalOrder.get();
                order.mergeWith(orderDTO);

                try {
                    orderService.completeOrderById(order.getOrderId());
                } catch (ServiceUnavailableException | ServiceException e) {
                    logger.warning(e.getMessage());
                }

                orderService.saveOrder(order);
            }
        }
    }
}