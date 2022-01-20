package de.ndhbr.ynvest.configuration;

import de.ndhbr.ynvest.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class LoggerConf {

    /**
     * @return instance of ynvest Logger
     */
    @Bean
    public Logger getLogger() {
        return Logger.getLogger(Constants.LOGGER);
    }
}
