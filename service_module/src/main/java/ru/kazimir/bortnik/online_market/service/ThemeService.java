package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

import java.util.List;

public interface ThemeService {

    List<ThemeDTO> getThemes();

    ThemeDTO getById(Long id);

    void add(ThemeDTO themeDTO);

    ThemeDTO getByName(String name);
}
