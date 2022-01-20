package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.enumeration.OrderType;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.ndhbr.ynvest.util.Constants;
import de.othr.sw.yetra.dto.OrderDTO;
import de.othr.sw.yetra.dto.ShareDetailsDTO;
import de.othr.sw.yetra.dto.TimePeriodDTO;
import de.othr.sw.yetra.entity.Share;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Scope("singleton")
public class StockExchangeClient implements StockExchangeClientIF {

    private final String stockExchangeNotAvailable = "Börse yetra nicht " +
            "erreichbar oder fehlerhaft.";

    @Autowired
    @Qualifier("stockExchange")
    WebClient webClient;

    @Override
    public List<Share> findSharesByKeyword(String query) throws ServiceUnavailableException, ServiceException {
        ResponseEntity<Share[]> response;
        List<Share> result = new ArrayList<>();

        try {
            response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/shares")
                            .queryParam("name", query).build())
                    .retrieve()
                    .toEntity(Share[].class)
                    .block();
        } catch (WebClientRequestException e) {
            throw new ServiceUnavailableException(stockExchangeNotAvailable);
        }

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            Share[] responseShare = response.getBody();

            if (responseShare != null) {
                result = Arrays.asList(responseShare);
            }
        } else {
            throw new ServiceException(stockExchangeNotAvailable);
        }

        return result;
    }

    @Override
    public List<Share> getSharesByIsins(List<String> isins) throws ServiceUnavailableException, ServiceException {
        ResponseEntity<Share[]> response;
        List<Share> result = new ArrayList<>();

        if (isins.size() > 0) {
            try {
                response = webClient
                        .get()
                        .uri(uriBuilder -> uriBuilder.path("/shares")
                                .queryParam("filter", isins).build())
                        .retrieve()
                        .toEntity(Share[].class)
                        .block(Constants.WEBCLIENT_TIMEOUT);
            } catch (WebClientRequestException e) {
                throw new ServiceUnavailableException(stockExchangeNotAvailable);
            }

            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                Share[] responseShare = response.getBody();

                if (responseShare != null) {
                    result = Arrays.asList(responseShare);
                }
            } else {
                throw new ServiceException(stockExchangeNotAvailable);
            }
        }

        return result;
    }

    @Override
    public ShareDetailsDTO getShareDetails(String isin, TimePeriodDTO timePeriod) throws ServiceUnavailableException,
            ServiceException {
        ResponseEntity<ShareDetailsDTO> response;
        ShareDetailsDTO result = null;

        try {
            response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/shares/" + isin)
                            .queryParam("timePeriod", timePeriod).build())
                    .retrieve()
                    .toEntity(ShareDetailsDTO.class)
                    .block(Constants.WEBCLIENT_TIMEOUT);
        } catch (WebClientRequestException e) {
            throw new ServiceUnavailableException(stockExchangeNotAvailable);
        }

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            ShareDetailsDTO responseShare = response.getBody();

            if (responseShare != null) {
                result = responseShare;
            }
        } else {
            throw new ServiceException(stockExchangeNotAvailable);
        }

        return result;
    }

    @Override
    public double getSharePrice(String isin) throws ServiceUnavailableException, ServiceException {
        ResponseEntity<ShareDetailsDTO> response;
        double result = 0.0;

        response = webClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/shares/" + isin).build())
                .retrieve()
                .toEntity(ShareDetailsDTO.class)
                .onErrorMap(WebClientRequestException.class,
                        e -> new ServiceUnavailableException(stockExchangeNotAvailable))
                .block(Constants.WEBCLIENT_TIMEOUT);

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            ShareDetailsDTO share = response.getBody();

            if (share != null) {
                result = share.getCurrentPrice();
            }
        } else {
            throw new ServiceException(stockExchangeNotAvailable);
        }

        return result;
    }

    @Override
    public StockOrder createOrder(StockOrder order) throws ServiceUnavailableException, ServiceException {
        ResponseEntity<OrderDTO> response;
        OrderDTO remoteOrder = new OrderDTO();

        remoteOrder.setIsin(order.getIsin());
        remoteOrder.setQuantity(order.getQuantity());
        remoteOrder.setUnitPrice(order.getUnitPrice());

        if (order.getType() == OrderType.Buy) {
            remoteOrder.setType(de.othr.sw.yetra.entity.OrderType.BUY);
        } else {
            remoteOrder.setType(de.othr.sw.yetra.entity.OrderType.SELL);
        }

        response = webClient
                .post()
                .uri(uriBuilder -> uriBuilder.path("/orders").build())
                .body(Mono.just(remoteOrder), OrderDTO.class)
                .retrieve()
                .toEntity(OrderDTO.class)
                .onErrorMap(WebClientRequestException.class,
                        e -> new ServiceUnavailableException(stockExchangeNotAvailable))
                .block(Constants.WEBCLIENT_TIMEOUT);

        if (response != null && response.getStatusCode() == HttpStatus.OK) {
            OrderDTO responseOrder = response.getBody();

            if (responseOrder != null && responseOrder.getTimestamp() != null &&
                    responseOrder.getStatus() != null) {
                order.mergeWith(responseOrder);
            }
        } else {
            throw new ServiceException(stockExchangeNotAvailable);
        }

        return order;
    }
}
