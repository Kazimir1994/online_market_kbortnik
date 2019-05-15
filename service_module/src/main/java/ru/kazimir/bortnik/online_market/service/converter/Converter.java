package ru.kazimir.bortnik.online_market.service.converter;

public interface Converter<ObjectDTO, Object> {
    ObjectDTO toDTO(Object object);

    Object fromDTO(ObjectDTO ObjectDTO);
}