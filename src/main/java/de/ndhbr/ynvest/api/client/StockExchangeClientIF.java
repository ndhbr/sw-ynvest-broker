package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.StockOrder;
import de.othr.sw.yetra.dto.ShareDetailsDTO;
import de.othr.sw.yetra.dto.TimePeriodDTO;
import de.othr.sw.yetra.entity.Share;

import java.util.List;

public interface StockExchangeClientIF {
    List<Share> findSharesByKeyword(String query);
    List<Share> getSharesByIsins(List<String> isins);
    ShareDetailsDTO getShareDetails(String isin, TimePeriodDTO timePeriod);
    double getSharePrice(String isin);
    StockOrder createOrder(StockOrder order);
}
