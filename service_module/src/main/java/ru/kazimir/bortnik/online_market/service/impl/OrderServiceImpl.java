package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.ItemRepository;
import ru.kazimir.bortnik.online_market.repository.OrderRepository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.model.Item;
import ru.kazimir.bortnik.online_market.repository.model.Order;
import ru.kazimir.bortnik.online_market.repository.model.OrderStatusEnum;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.OrderService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.ItemServiceException;
import ru.kazimir.bortnik.online_market.service.exception.OrderServiceException;
import ru.kazimir.bortnik.online_market.service.exception.UserServiceException;
import ru.kazimir.bortnik.online_market.service.model.OrderDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_ORDERS;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_GET_ITEM;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_GET_USER;

@Service
public class OrderServiceImpl implements OrderService {
    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Converter<OrderDTO, Order> orderConverter;
    private final Converter<OrderDTO, Order> orderMoreConverter;
    private static final long ORDER_NUMBER_DIVIDER = 1000000000;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            ItemRepository itemRepository,
                            @Qualifier("orderConverterImpl") Converter<OrderDTO, Order> orderConverter,
                            @Qualifier("orderMoreConverterImpl") Converter<OrderDTO, Order> orderMoreConverter) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.orderConverter = orderConverter;
        this.orderMoreConverter = orderMoreConverter;
    }

    @Override
    @Transactional
    public void add(OrderDTO orderDTO) {
        Order order = new Order();
        order.setQuantity(orderDTO.getQuantity());
        User user = userRepository.findById(orderDTO.getUser().getId());
        if (user != null) {
            order.setUser(user);
            Item item = itemRepository.findById(orderDTO.getItem().getId());
            if (item != null) {
                order.setItem(item);
                order.setDataCreation(new Date());
                order.setStatus(OrderStatusEnum.NEW);
                order.setOrderNumber(getOrderNumber());
                orderRepository.persist(order);
            } else {
                logger.info(USER_ERROR_GET_ITEM, orderDTO.getItem().getId());
                throw new ItemServiceException(String.format(USER_ERROR_GET_ITEM, orderDTO.getItem().getId()));
            }
        } else {
            logger.info(USER_ERROR_GET_USER, orderDTO.getUser().getId());
            throw new UserServiceException(String.format(USER_ERROR_GET_USER, orderDTO.getUser().getId()));
        }
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrdersById(Long limitPositions, Long currentPage, Long customerUserId) {
        PageDTO<OrderDTO> orderPageDTO = new PageDTO<>();
        orderPageDTO.setCurrentPage(currentPage);
        Long countOfOrders = orderRepository.getCountOfOrdersById(customerUserId);
        Long countOfPages = calculationCountOfPages(countOfOrders, limitPositions);
        orderPageDTO.setCountOfPages(countOfPages);

        if (currentPage > countOfPages) {
            currentPage = countOfPages;
        } else if (currentPage < countOfPages) {
            currentPage = 1L;
        }
        Long offset = getPosition(limitPositions, currentPage);
        logger.info(MESSAGES_GET_ORDERS, limitPositions, offset, customerUserId);
        List<Order> orderList = orderRepository.getOrdersByUserId(offset, limitPositions, customerUserId);

        List<OrderDTO> orderDTOList = listToListDTO(orderList);

        orderPageDTO.setList(orderDTOList);
        return orderPageDTO;
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrders(Long limitPositions, Long currentPage) {
        PageDTO<OrderDTO> orderPageDTO = new PageDTO<>();
        orderPageDTO.setCurrentPage(currentPage);
        Long countOfOrders = orderRepository.getCountOfEntities();
        Long countOfPages = calculationCountOfPages(countOfOrders, limitPositions);
        orderPageDTO.setCountOfPages(countOfPages);
        if (currentPage > countOfPages) {
            currentPage = countOfPages;
        } else if (currentPage < countOfPages) {
            currentPage = 1L;
        }
        Long offset = getPosition(limitPositions, currentPage);
        logger.info(MESSAGES_GET_ORDERS, limitPositions, offset);
        List<Order> orderList = orderRepository.findAll(offset, limitPositions);
        List<OrderDTO> orderDTOList = listToListDTO(orderList);
        orderPageDTO.setList(orderDTOList);
        return orderPageDTO;
    }

    @Override
    @Transactional
    public OrderDTO getMoreByID(Long id) {
        Order order = orderRepository.findById(id);
        if (order != null) {
            OrderDTO orderDTO = orderMoreConverter.toDTO(order);
            BigDecimal priceMultiplier = BigDecimal.valueOf(order.getQuantity());
            BigDecimal totalPrice = order.getItem().getPrice().multiply(priceMultiplier);
            orderDTO.setTotalPrice(totalPrice);
            return orderDTO;
        } else {
            throw new OrderServiceException();
        }
    }

    @Override
    @Transactional
    public void updateStatus(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.getId());
        if (order != null) {
            order.setStatus(orderDTO.getStatus());
            orderRepository.merge(order);
        } else {
            throw new OrderServiceException();
        }
    }

    @Override
    public List<OrderDTO> getListOrders(Long offset, Long limit) {
        List<Order> orderList = orderRepository.findAll(offset, limit);
        return ListOrderToListMoreOrderDTO(orderList);
    }

    private Long getPosition(Long limitPositions, Long positions) {
        if (positions == 0) {
            positions++;
        }
        return limitPositions * (positions - 1);
    }

    private Long calculationCountOfPages(Long countOfArticle, Long limitPositions) {
        Long countOfPages;
        if (countOfArticle % limitPositions > 0) {
            countOfPages = (countOfArticle / limitPositions) + 1;
        } else {
            countOfPages = countOfArticle / limitPositions;
        }
        return countOfPages;
    }

    private Long getOrderNumber() {
        return System.currentTimeMillis() % ORDER_NUMBER_DIVIDER;
    }

    private List<OrderDTO> listToListDTO(List<Order> orderList) {
        return orderList.stream().map(order -> {
            OrderDTO orderDTO = orderConverter.toDTO(order);
            orderDTO.setTotalPrice(getTotalPrice(order));
            return orderDTO;
        }).collect(Collectors.toList());
    }

    private List<OrderDTO> ListOrderToListMoreOrderDTO(List<Order> orderList) {
        return orderList.stream().map(order -> {
            OrderDTO orderDTO = orderMoreConverter.toDTO(order);
            orderDTO.setTotalPrice(getTotalPrice(order));
            return orderDTO;
        }).collect(Collectors.toList());
    }

    private BigDecimal getTotalPrice(Order order) {
        BigDecimal priceMultiplier = BigDecimal.valueOf(order.getQuantity());
        return order.getItem().getPrice().multiply(priceMultiplier);
    }
}
