package de.ndhbr.ynvest.repository;

import de.ndhbr.ynvest.entity.Customer;
import de.ndhbr.ynvest.entity.StockOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends PagingAndSortingRepository<StockOrder, Long> {
    @Query("SELECT SUM((CASE WHEN type = 'Sell' THEN -1 ELSE 1 END *" +
            "quantity * unitPrice) + 0.99) FROM StockOrder WHERE customer = :#{#customer} " +
            "AND status = 'Open'")
    double getSumOfAllOpenOrdersByCustomer(@Param("customer") Customer customer);
}
