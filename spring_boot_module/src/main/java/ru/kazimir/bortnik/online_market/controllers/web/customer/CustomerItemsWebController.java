package ru.kazimir.bortnik.online_market.controllers.web.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kazimir.bortnik.online_market.service.ItemService;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;
import ru.kazimir.bortnik.online_market.service.model.OrderDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.Pageable;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ITEMS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ITEMS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ITEMS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ITEM_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_ITEM_SHOWING_MORE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_422;

@Controller
@RequestMapping(PUBLIC_CUSTOMER_ITEMS_URL)
public class CustomerItemsWebController {
    private final static Logger logger = LoggerFactory.getLogger(CustomerItemsWebController.class);

    private final ItemService itemService;
    private final Pageable pageable = new Pageable(10L);

    @Autowired
    public CustomerItemsWebController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(PUBLIC_CUSTOMER_ITEMS_SHOWING_URL)
    public String showItems(@RequestParam(defaultValue = "1") Long currentPage, OrderDTO orderDTO, Model model) {
        PageDTO<ItemDTO> items = itemService.getItems(pageable.getLimitPositions(), currentPage);
        logger.info("List of Items for showing := {}", items.getList());
        model.addAttribute("PageDTO", items);
        return PUBLIC_CUSTOMER_ITEMS_PAGE;
    }


    @GetMapping(PUBLIC_CUSTOMER_ITEM_SHOWING_MORE_URL)
    public String showMoreItem(@RequestParam(required = false, defaultValue = "0") Long itemId, Model model) {
        if (itemId != null && itemId > 0) {
            ItemDTO ItemDTO = itemService.getById(itemId);
            model.addAttribute("Item", ItemDTO);
            logger.info("Item to display := {} ", ItemDTO);
            return PUBLIC_CUSTOMER_ITEM_PAGE;

        } else {
            return REDIRECT_ERROR_422;
        }
    }
}
