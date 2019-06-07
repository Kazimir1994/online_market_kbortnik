package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Order;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.OrderDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class OrderMoreConverterImpl implements Converter<OrderDTO, Order> {
    private final Converter<OrderDTO, Order> orderConverter;
    private final Converter<UserDTO, User> userOrderConverter;

    public OrderMoreConverterImpl(@Qualifier("orderConverterImpl") Converter<OrderDTO, Order> orderConverter,
                                  @Qualifier("userOrderConverterImpl") Converter<UserDTO, User> userOrderConverter) {
        this.orderConverter = orderConverter;
        this.userOrderConverter = userOrderConverter;
    }

    @Override
    public OrderDTO toDTO(Order order) {
        OrderDTO orderDTO = orderConverter.toDTO(order);
        UserDTO userDTO = userOrderConverter.toDTO(order.getUser());
        orderDTO.setUser(userDTO);
        return orderDTO;
    }

    @Override
    public Order fromDTO(OrderDTO ObjectDTO) {
        throw new UnsupportedOperationException();
    }
}
