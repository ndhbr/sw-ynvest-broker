package de.ndhbr.ynvest.util;

import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

public class Constants {

    /**
     * Fee, which is charged after each order
     */
    public static final double TRANSACTION_FEE = 0.99;

    /**
     * JMS Queue to complete orders
     */
    public static final String ORDER_QUEUE = "sw_andreas_huber_queue_ynvest_orders";

    /**
     * Logger identifier
     */
    public static final String LOGGER = "ynvest_logger";

    /**
     * Timeout for webclient requests
     */
    public static final Duration WEBCLIENT_TIMEOUT = Duration.ofSeconds(15);

    /**
     * Fixed eBank bank account IBAN for ynvest
     */
    @Value("${eBank.iban}")
    public static final String YNVEST_IBAN = "DE176141179";
}
