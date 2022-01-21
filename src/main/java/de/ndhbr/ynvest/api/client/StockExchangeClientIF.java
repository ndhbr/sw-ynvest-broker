package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.exception.ServiceUnavailableException;
import de.othr.sw.yetra.dto.ShareDetailsDTO;
import de.othr.sw.yetra.dto.TimePeriodDTO;
import de.othr.sw.yetra.entity.Share;
import org.hibernate.service.spi.ServiceException;

import java.util.List;

public interface StockExchangeClientIF {
    /**
     * Search for shares by keyword
     * @param query Search query
     * @return List of found shares
     * @throws ServiceUnavailableException If stock exchange is not available
     * @throws ServiceException If other error occurs
     */
    List<Share> findSharesByKeyword(String query) throws ServiceUnavailableException, ServiceException;

    /**
     * Get shares by ISIN
     * @param isins List of ISINs
     * @return List of found shares
     * @throws ServiceUnavailableException If stock exchange is not available
     * @throws ServiceException If other error occurs
     */
    List<Share> getSharesByIsins(List<String> isins) throws ServiceUnavailableException, ServiceException;

    /**
     * Get details about share (name, market values, ...) by ISIN
     * @param isin ISIN
     * @param timePeriod Day, Week, Month, Year
     * @return Details about Share
     * @throws ServiceUnavailableException If stock exchange is not available
     * @throws ServiceException If other error occurs
     */
    ShareDetailsDTO getShareDetails(String isin, TimePeriodDTO timePeriod) throws ServiceUnavailableException,
            ServiceException;

    /**
     * Get current price of share by ISIN
     * @param isin ISIN
     * @return Current stock price
     * @throws ServiceUnavailableException If stock exchange is not available
     * @throws ServiceException If other error occurs
     */
    double getSharePrice(String isin) throws ServiceUnavailableException, ServiceException;

    /**
     * Creates order in stock exchange
     * @param order Stock order
     * @return Stock order with order id
     * @throws ServiceUnavailableException If stock exchange is not available
     * @throws ServiceException If other error occurs
     */
    StockOrder createOrder(StockOrder order) throws ServiceUnavailableException, ServiceException;
}
