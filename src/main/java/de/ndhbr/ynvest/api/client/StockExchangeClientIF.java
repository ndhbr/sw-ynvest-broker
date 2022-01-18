package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.othr.sw.yetra.dto.ShareDetailsDTO;
import de.othr.sw.yetra.dto.TimePeriodDTO;
import de.othr.sw.yetra.entity.Share;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface StockExchangeClientIF {
    List<Share> findSharesByKeyword(String query) throws ServiceUnavailableException, ServiceException;

    List<Share> getSharesByIsins(List<String> isins) throws ServiceUnavailableException, ServiceException;

    ShareDetailsDTO getShareDetails(String isin, TimePeriodDTO timePeriod) throws ServiceUnavailableException,
            ServiceException;

    double getSharePrice(String isin) throws ServiceUnavailableException, ServiceException;

    StockOrder createOrder(StockOrder order) throws ServiceUnavailableException, ServiceException;
}
