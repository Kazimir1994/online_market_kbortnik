package ru.kazimir.bortnik.online_market.controllers.web.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.service.OrderService;
import ru.kazimir.bortnik.online_market.service.model.OrderDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.Pageable;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ORDERS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ORDERS_SHOWING_MORE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ORDERS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ORDERS_UPDATE_STATUS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ORDERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ORDER_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_REDIRECT_ORDERS_SHOWING_MORE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_422;

@Controller
@RequestMapping(PUBLIC_SALE_ORDERS_URL)
public class SaleOrdersWebController {
    private final static Logger logger = LoggerFactory.getLogger(SaleOrdersWebController.class);
    private final OrderService orderService;
    private final Pageable pageable = new Pageable(10L);

    public SaleOrdersWebController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(PUBLIC_SALE_ORDERS_SHOWING_URL)
    public final String showingOrders(@RequestParam(defaultValue = "1") Long currentPage, Model model) {

        PageDTO<OrderDTO> orderDTOPageDTO = orderService.getOrders(pageable.getLimitPositions(),
                currentPage);
        logger.info("List of Orders for showing := {}", orderDTOPageDTO.getList());
        model.addAttribute("PageDTO", orderDTOPageDTO);
        return PUBLIC_SALE_ORDERS_PAGE;
    }

    @GetMapping(PUBLIC_SALE_ORDERS_SHOWING_MORE_URL)
    public String showMoreOrder(@RequestParam(required = false, defaultValue = "0") Long orderId, Model model) {
        if (orderId != null && orderId > 0) {
            OrderDTO orderDTO = orderService.getMoreByID(orderId);
            logger.info("Order to display := {} ", orderDTO);
            model.addAttribute("Order", orderDTO);
            return PUBLIC_SALE_ORDER_PAGE;

        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @PostMapping(PUBLIC_SALE_ORDERS_UPDATE_STATUS_URL)
    public String showMoreOrder(OrderDTO Order, RedirectAttributes redirectAttributes) {
        logger.info("Order status change request {} ", Order);
        orderService.updateStatus(Order);
        redirectAttributes.addFlashAttribute("message", "Status changed successfully");
        return String.format(PUBLIC_SALE_REDIRECT_ORDERS_SHOWING_MORE_URL, Order.getId());
    }

}
