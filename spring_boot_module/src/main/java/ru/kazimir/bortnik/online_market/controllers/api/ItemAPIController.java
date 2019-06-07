package ru.kazimir.bortnik.online_market.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kazimir.bortnik.online_market.service.ItemService;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

import javax.validation.Valid;
import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ITEMS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ITEM_ADD_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ITEM_DELETE_ID_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ITEM_SHOWING_ID_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ITEM_URL;

@RestController
@RequestMapping(API_ITEM_URL)
public class ItemAPIController {
    private final static Logger logger = LoggerFactory.getLogger(ItemAPIController.class);
    private final ItemService itemService;

    @Autowired
    public ItemAPIController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping(API_ITEMS_SHOWING_URL)
    public ResponseEntity<List<ItemDTO>> getItems(
            @RequestParam(name = "limit", defaultValue = "10") Long limit,
            @RequestParam(name = "offset", defaultValue = "0") Long offset) {
        logger.info("Request for receiving items from the position := {}, quantity := {}.", limit, offset);
        List<ItemDTO> itemDTOList = itemService.getListItems(offset, limit);
        logger.info("Send a list of items. := {}.", itemDTOList);
        return new ResponseEntity<>(itemDTOList, HttpStatus.OK);
    }

    @GetMapping(API_ITEM_SHOWING_ID_URL)
    public ResponseEntity<ItemDTO> getItem(@PathVariable("id") Long id) {
        logger.info("Request for news by id := {}.", id);
        ItemDTO itemDTO = itemService.getById(id);
        logger.info("Send a list of article. := {}.", itemDTO);
        return new ResponseEntity<>(itemDTO, HttpStatus.OK);
    }

    @DeleteMapping(API_ITEM_DELETE_ID_URL)
    public ResponseEntity deleteItem(@PathVariable("id") Long id) {
        logger.info("Requests delete Item  by id := {}.", id);
        itemService.deleteById(id);
        logger.info("Item removed successfully.");
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping(API_ITEM_ADD_URL)
    public ResponseEntity addItem(@RequestBody @Valid ItemDTO itemDTO,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            logger.info("error := {}.", bindingResult.getAllErrors());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        logger.info("Add new item {}", itemDTO);
        itemService.add(itemDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
