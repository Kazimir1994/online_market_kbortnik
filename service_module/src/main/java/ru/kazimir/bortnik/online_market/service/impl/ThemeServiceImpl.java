package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.ThemeRepository;
import ru.kazimir.bortnik.online_market.repository.model.Theme;
import ru.kazimir.bortnik.online_market.service.ThemeService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_THEME;

@Service
public class ThemeServiceImpl implements ThemeService {
    private final static Logger logger = LoggerFactory.getLogger(ThemeServiceImpl.class);
    private final Converter<ThemeDTO, Theme> themeConverter;
    private final ThemeRepository themeRepository;

    public ThemeServiceImpl(Converter<ThemeDTO, Theme> themeConverter,
                            ThemeRepository themeRepository) {
        this.themeConverter = themeConverter;
        this.themeRepository = themeRepository;
    }

    @Transactional
    @Override
    public List<ThemeDTO> getThemes() {
        List<Theme> themeList = themeRepository.findAll();
        List<ThemeDTO> themeDTOList = themeList.stream().map(themeConverter::toDTO).collect(Collectors.toList());
        logger.info(MESSAGES_GET_THEME, themeDTOList);
        return themeDTOList;
    }

    @Transactional
    @Override
    public ThemeDTO getById(Long id) {
        Theme theme = themeRepository.findById(id);
        return themeConverter.toDTO(theme);
    }

    @Transactional
    @Override
    public void add(ThemeDTO themeDTO) {
        Theme theme = themeConverter.fromDTO(themeDTO);
        themeRepository.persist(theme);
    }

    @Transactional
    @Override
    public ThemeDTO getByName(String name) {
        Theme theme = themeRepository.getByName(name);
        if (theme != null) {
            return themeConverter.toDTO(theme);
        } else {
            return null;
        }
    }
}
