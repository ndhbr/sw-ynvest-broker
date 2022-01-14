package de.ndhbr.ynvest.jms;

import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
/* TODO: Remove
@Component
public class SenderService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendOrder(StockOrder stockOrder) {
        jmsTemplate.convertAndSend(Constants.ORDER_QUEUE, stockOrder);
    }
}
*/