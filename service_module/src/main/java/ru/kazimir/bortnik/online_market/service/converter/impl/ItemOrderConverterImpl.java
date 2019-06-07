package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Item;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ItemDTO;

@Component
public class ItemOrderConverterImpl implements Converter<ItemDTO, Item> {

    @Override
    public ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setName(item.getName());
        itemDTO.setPrice(item.getPrice().toString());
        return itemDTO;
    }

    @Override
    public Item fromDTO(ItemDTO ObjectDTO) {
        throw new UnsupportedOperationException();
    }
}
