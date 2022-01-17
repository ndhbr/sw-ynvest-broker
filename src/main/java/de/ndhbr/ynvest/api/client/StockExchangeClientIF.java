package de.ndhbr.ynvest.api.client;

import de.ndhbr.ynvest.entity.StockOrder;
import de.othr.sw.yetra.dto.ShareDetailsDTO;

import java.util.List;

public interface StockExchangeClientIF {
    List<ShareDetailsDTO> findSharesByKeyword(String query);
    List<ShareDetailsDTO> getSharesByIsins(List<String> isins);
    ShareDetailsDTO getShareDetails(String isin);
    double getSharePrice(String isin);
    StockOrder createOrder(StockOrder order);
}
