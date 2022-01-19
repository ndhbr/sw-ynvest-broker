package de.ndhbr.ynvest.repository;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import de.ndhbr.ynvest.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends PagingAndSortingRepository<StockOrder, Long> {
    @Query("SELECT COALESCE(SUM((CASE WHEN type = 'Sell' THEN -1 ELSE 1 END *" +
            "quantity * unitPrice) + " + Constants.TRANSACTION_FEE +
            "), 0) FROM StockOrder WHERE customer = :#{#customer} AND status = 'Open'")
    double getSumOfAllOpenOrdersByCustomer(@Param("customer") Customer customer);

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM StockOrder WHERE customer = :#{#customer}" +
            " AND status = 'Open'")
    int getQuantityOfAllOpenSellOrdersByCustomer(@Param("customer") Customer customer);

    @Query("FROM StockOrder WHERE customer = :#{#customer} AND isin = :#{#isin}" +
            " AND status = 'Open' ORDER BY placedOn DESC")
    List<StockOrder> getOpenOrdersByIsin(@Param("customer") Customer customer, String isin);

    Page<StockOrder> findStockOrderByCustomer(Customer customer, Pageable pageable);
}