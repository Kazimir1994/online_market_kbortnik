package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Item;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

import java.math.BigDecimal;

@Component
public class ItemConverterImpl implements Converter<ItemDTO, Item> {

    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice().toString());
        itemDTO.setDeleted(item.isDeleted());
        itemDTO.setShortDescription(item.getShortDescription());
        itemDTO.setUniqueNumber(item.getUniqueNumber());
        return itemDTO;
    }

    @Override
    public Item fromDTO(ItemDTO itemDTO) {
        Item item = new Item();
        item.setId(itemDTO.getId());
        item.setName(itemDTO.getName());
        item.setPrice(BigDecimal.valueOf(Double.valueOf(itemDTO.getPrice())));
        item.setDeleted(itemDTO.isDeleted());
        item.setShortDescription(itemDTO.getShortDescription());
        item.setUniqueNumber(itemDTO.getUniqueNumber());
        return item;
    }
}
