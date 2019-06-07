package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    Long getCountOfOrdersById(Long id);

    List<Order> getOrdersByUserId(Long offset, Long limitPositions, Long id);

    List<Order> findAll(Long offset, Long limit);
}
