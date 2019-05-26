package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.ItemDTO;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;

import java.util.List;

public interface ItemService {

    PageDTO<ItemDTO> getItems(Long limitPositions, Long currentPage);

    List<ItemDTO> getListItems(Long offset, Long limit);

    ItemDTO getById(Long id);

    void deleteById(Long id);

    void add(ItemDTO itemDTO);

}
