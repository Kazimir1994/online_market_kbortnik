package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Theme;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

@Component
public class ThemeConverterImpl implements Converter<ThemeDTO, Theme> {

    @Override
    public ThemeDTO toDTO(Theme theme) {
        ThemeDTO themeDTO = new ThemeDTO();
        themeDTO.setId(theme.getId());
        themeDTO.setName(theme.getName());
        return themeDTO;
    }

    @Override
    public Theme fromDTO(ThemeDTO themeDTO) {
        Theme theme = new Theme();
        theme.setId(themeDTO.getId());
        theme.setName(themeDTO.getName());
        return theme;
    }
}
