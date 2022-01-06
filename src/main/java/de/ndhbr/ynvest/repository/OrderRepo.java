package de.ndhbr.ynvest.repository;

import de.ndhbr.ynvest.entity.StockOrder;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends PagingAndSortingRepository<StockOrder, Long> { }
