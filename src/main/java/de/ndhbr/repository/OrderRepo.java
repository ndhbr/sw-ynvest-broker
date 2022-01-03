package de.ndhbr.repository;

import de.ndhbr.entity.StockOrder;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends PagingAndSortingRepository<StockOrder, String> {

}
