package de.ndhbr.ynvest.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

public class Constants {
    public static final double TRANSACTION_FEE = 0.99;
    public static final String ORDER_QUEUE = "sw_andreas_huber_queue_ynvest_orders";
    public static final String LOGGER = "ynvest_logger";
    public static final Duration WEBCLIENT_TIMEOUT = Duration.ofSeconds(15);
    @Value("${iban}")
    public static final String YNVEST_IBAN = "";
}
