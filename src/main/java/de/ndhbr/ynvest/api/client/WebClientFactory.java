package de.ndhbr.ynvest.api.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientFactory {

    @Bean
    @Qualifier("stockExchange")
    public WebClient getStockExchangeWebClient() {
        return generateWebClient()
                .baseUrl("http://im-codd.oth-regensburg.de:8922/api")
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("ynvest", "123"))
                .build();
    }

    @Bean
    @Qualifier("bank")
    public WebClient getBankWebClient() {
        return generateWebClient()
                .baseUrl("http://im-codd.oth-regensburg.de:8937/restapi")
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth("ynvest", "123"))
                .build();
    }

    private WebClient.Builder generateWebClient() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }
}
