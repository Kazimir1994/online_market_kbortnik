package ru.kazimir.bortnik.online_market.controllers.web.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.service.OrderService;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;
import ru.kazimir.bortnik.online_market.service.model.OrderDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.Pageable;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDetail;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ADD_ORDER;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ORDERS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ORDERS_SHOWING_USER_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ORDERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_REDIRECT_ITEMS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_422;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_LOGIN_URL;

@Controller
@RequestMapping(PUBLIC_CUSTOMER_ORDERS_URL)
public class CustomerOrdersWebController {
    private final static Logger logger = LoggerFactory.getLogger(CustomerOrdersWebController.class);
    private final OrderService orderService;
    private final Pageable pageable = new Pageable(10L);

    public CustomerOrdersWebController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(PUBLIC_CUSTOMER_ORDERS_SHOWING_USER_URL)
    public final String showingOrders(@RequestParam(defaultValue = "1") Long currentPage, Model model,
                                      @AuthenticationPrincipal UserDetail UserDetail) {
        if (UserDetail.getUserDTO() != null) {
            PageDTO<OrderDTO> orderDTOPageDTO = orderService.getOrdersById(pageable.getLimitPositions(),
                    currentPage, UserDetail.getUserDTO().getId());
            logger.info("List of Orders for showing := {}", orderDTOPageDTO.getList());
            model.addAttribute("PageDTO", orderDTOPageDTO);
        } else {
            return REDIRECT_LOGIN_URL;
        }
        return PUBLIC_CUSTOMER_ORDERS_PAGE;
    }

    @PostMapping(PUBLIC_CUSTOMER_ADD_ORDER)
    public final String saveOrder(OrderDTO orderDTO,
                                  @RequestParam(defaultValue = "0") Long itemId,
                                  @AuthenticationPrincipal UserDetail UserDetail,
                                  RedirectAttributes redirectAttributes) {
        logger.info("Order creation request. Id Item:= {}, Id User:= {}, Order:= {}", itemId,
                UserDetail.getUserDTO().getId(), orderDTO);

        if (itemId != null && itemId > 0) {
            if (UserDetail.getUserDTO() != null) {
                orderDTO.setItem(new ItemDTO(itemId));
                orderDTO.setUser(new UserDTO(UserDetail.getUserDTO().getId()));
                orderService.add(orderDTO);
                redirectAttributes.addFlashAttribute("message", "Your order has been successfully created");
                return PUBLIC_CUSTOMER_REDIRECT_ITEMS_SHOWING_URL;
            } else {
                return REDIRECT_LOGIN_URL;
            }
        } else {
            return REDIRECT_ERROR_422;
        }
    }
}
