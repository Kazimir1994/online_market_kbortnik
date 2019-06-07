package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Item;
import ru.kazimir.bortnik.online_market.repository.model.Order;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;
import ru.kazimir.bortnik.online_market.service.model.OrderDTO;

@Component
public class OrderConverterImpl implements Converter<OrderDTO, Order> {
    private final Converter<ItemDTO, Item> ItemOrderConverter;

    public OrderConverterImpl(@Qualifier("itemOrderConverterImpl") Converter<ItemDTO, Item> itemOrderConverter) {
        ItemOrderConverter = itemOrderConverter;
    }

    @Override
    public OrderDTO toDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderNumber(order.getOrderNumber());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setQuantity(order.getQuantity());
        orderDTO.setItem(ItemOrderConverter.toDTO(order.getItem()));
        return orderDTO;
    }

    @Override
    public Order fromDTO(OrderDTO orderDTO) {
        throw new UnsupportedOperationException();
    }
}
