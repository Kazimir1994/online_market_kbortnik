package ru.kazimir.bortnik.online_market.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kazimir.bortnik.online_market.service.ThemeService;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_SALE_THEME_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_THEMES_SAVE_URL;

@RestController
@RequestMapping(API_SALE_THEME_URL)
public class ThemeAPIController {
    private final static Logger logger = LoggerFactory.getLogger(ItemAPIController.class);
    private final Validator validator;
    private final ThemeService themeService;

    @Autowired
    public ThemeAPIController(@Qualifier("addThemeValidatorImpl") Validator validator,
                              ThemeService themeService) {
        this.validator = validator;
        this.themeService = themeService;
    }

    @PostMapping(API_THEMES_SAVE_URL)
    public ResponseEntity addTheme(@RequestBody ThemeDTO themeDTO, BindingResult bindingResult) {
        logger.info("Request API to add a theme ( theme := {}. )", themeDTO);
        validator.validate(themeDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("Error := {}", bindingResult.getAllErrors());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        themeService.add(themeDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
