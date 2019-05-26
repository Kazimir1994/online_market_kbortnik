package ru.kazimir.bortnik.online_market.controllers.web.sale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.controllers.web.admin.AdminUsersWebController;
import ru.kazimir.bortnik.online_market.service.ItemService;
import ru.kazimir.bortnik.online_market.service.exception.ItemServiceException;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.Pageable;

import javax.validation.Valid;
import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_DELETE_ITEMS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEMS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEMS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEMS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEM_COPY_FORM_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEM_COPY_FORM_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEM_COPY_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEM_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_ITEM_SHOWING_MORE_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_REDIRECT_ITEMS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_SALE_REDIRECT_ITEM_COPY_FORM_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_404;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_422;

@Controller
@RequestMapping(PUBLIC_SALE_ITEMS_URL)
public class SaleItemController {
    private final static Logger logger = LoggerFactory.getLogger(AdminUsersWebController.class);

    private final ItemService itemService;
    private final Pageable pageable = new Pageable(10L);

    @Autowired
    public SaleItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(PUBLIC_SALE_ITEMS_SHOWING_URL)
    public String showItems(@RequestParam(defaultValue = "1") Long currentPage, Model model) {
        PageDTO<ItemDTO> items = itemService.getItems(pageable.getLimitPositions(), currentPage);
        logger.info("List of Items for showing := {}", items.getList());
        model.addAttribute("PageDTO", items);
        return PUBLIC_SALE_ITEMS_PAGE;
    }

    @PostMapping(PUBLIC_SALE_DELETE_ITEMS_URL)
    public String deleteItem(@RequestParam(required = false, defaultValue = "0") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Request to delete Item by ID {}.", id);
        if (id != null && id != 0) {
            try {
                itemService.deleteById(id);
                redirectAttributes.addFlashAttribute("message", "Item successfully deleted");
                return PUBLIC_SALE_REDIRECT_ITEMS_SHOWING_URL;
            } catch (ItemServiceException e) {
                logger.info("Item by id:= {} not found.", id);
                return REDIRECT_ERROR_404;
            }
        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @GetMapping(PUBLIC_SALE_ITEM_SHOWING_MORE_URL)
    public String showMoreItem(@RequestParam(required = false, defaultValue = "0") Long itemId, Model model) {
        if (itemId != null && itemId != 0) {
            try {
                ItemDTO ItemDTO = itemService.getById(itemId);
                model.addAttribute("Item", ItemDTO);
                logger.info("Item to display := {} ", ItemDTO);

                return PUBLIC_SALE_ITEM_PAGE;
            } catch (ItemServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_ERROR_404;
            }
        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @GetMapping(PUBLIC_SALE_ITEM_COPY_FORM_URL)
    public String FormCopyingItem(@RequestParam(required = false, defaultValue = "0") Long itemId, ItemDTO itemDTO,
                                  BindingResult results, Model model) {
        logger.info("Request to copying Item by ID {}.", itemId);
        if (itemId != null && itemId != 0) {
            try {
                ItemDTO ItemDTO = itemService.getById(itemId);
                logger.info("Item to copy := {} ", ItemDTO);
                model.addAttribute("Item", ItemDTO);

                if (model.containsAttribute("error")) {
                    for (ObjectError error : ((List<ObjectError>) model.asMap().get("error"))) {
                        results.addError(error);
                    }
                }
                return PUBLIC_SALE_ITEM_COPY_FORM_PAGE;
            } catch (ItemServiceException e) {
                logger.error(e.getMessage(), e);
                return REDIRECT_ERROR_404;
            }
        } else {
            return REDIRECT_ERROR_422;
        }
    }

    @PostMapping(PUBLIC_SALE_ITEM_COPY_URL)
    public String copyingItem(@RequestParam(required = false) Long itemId,
                              @Valid ItemDTO itemDTO,
                              BindingResult results,
                              RedirectAttributes redirectAttributes) {
        if (itemId != null && itemId != 0) {
            logger.info("Request to copying item := {}, id:={}", itemDTO, itemId);
            if (results.hasErrors()) {
                redirectAttributes.addFlashAttribute("error", results.getAllErrors());
                return String.format(PUBLIC_SALE_REDIRECT_ITEM_COPY_FORM_URL, itemId);
            }
            itemService.add(itemDTO);
            redirectAttributes.addFlashAttribute("message", "Item successfully copied");
            return String.format(PUBLIC_SALE_REDIRECT_ITEM_COPY_FORM_URL, itemId);
        } else {
            return REDIRECT_ERROR_422;
        }
    }
}
