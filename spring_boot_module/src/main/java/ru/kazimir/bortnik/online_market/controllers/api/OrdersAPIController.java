package ru.kazimir.bortnik.online_market.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kazimir.bortnik.online_market.service.OrderService;
import ru.kazimir.bortnik.online_market.service.model.OrderDTO;

import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ORDERS_SHOWING_ID_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ORDERS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ORDERS_URL;

@RestController
@RequestMapping(API_ORDERS_URL)
public class OrdersAPIController {
    private final static Logger logger = LoggerFactory.getLogger(OrdersAPIController.class);
    private final OrderService orderService;

    public OrdersAPIController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(API_ORDERS_SHOWING_URL)
    public ResponseEntity<List<OrderDTO>> getOrders(
            @RequestParam(name = "limit", defaultValue = "10") Long limit,
            @RequestParam(name = "offset", defaultValue = "0") Long offset) {
        logger.info("Request for receiving Orders from the position := {}, quantity := {}.", limit, offset);
        List<OrderDTO> itemDTOList = orderService.getListOrders(offset, limit);
        logger.info("Send a list of Orders. := {}.", itemDTOList);
        return new ResponseEntity<>(itemDTOList, HttpStatus.OK);
    }

    @GetMapping(API_ORDERS_SHOWING_ID_URL)
    public ResponseEntity<OrderDTO> getOrder(@PathVariable("id") Long id) {
        logger.info("Request for Order by id := {}.", id);
        OrderDTO orderDTO = orderService.getMoreByID(id);
        logger.info("Order:= {}.", orderDTO);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
}
