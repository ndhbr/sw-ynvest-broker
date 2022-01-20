package de.ndhbr.ynvest.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientFactory {

    /**
     * @return instance of
     */
    @Bean
    @Scope("singleton")
    @Qualifier("stockExchange")
    public WebClient getStockExchangeWebClient() {
        return generateWebClient()
                .baseUrl("http://im-codd.oth-regensburg.de:8922/api")
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("ynvest", "123"))
                .build();
    }

    @Bean
    @Scope("singleton")
    @Qualifier("bank")
    public WebClient getBankWebClient() {
        return generateWebClient()
                .baseUrl("http://im-codd.oth-regensburg.de:8937/restapi")
                .defaultHeaders(httpHeaders ->
                        httpHeaders.setBasicAuth("ynvest@othr.de", "ynvest"))
                .build();
    }

    private WebClient.Builder generateWebClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
}
