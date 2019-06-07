package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.OrderDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;

import java.util.List;

public interface OrderService {

    void add(OrderDTO orderDTO);

    PageDTO<OrderDTO> getOrdersById(Long limitPositions, Long currentPage, Long customerUserId);

    PageDTO<OrderDTO> getOrders(Long limitPositions, Long currentPage);

    OrderDTO getMoreByID(Long id);

    void updateStatus(OrderDTO orderDTO);

    List<OrderDTO> getListOrders(Long offset, Long limit);
}
