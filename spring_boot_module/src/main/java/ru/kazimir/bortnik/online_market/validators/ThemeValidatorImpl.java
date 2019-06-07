package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.ThemeService;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

@Component
public class ThemeValidatorImpl implements Validator {

    private final ThemeService themeService;

    @Autowired
    public ThemeValidatorImpl(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ThemeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            ThemeDTO themeDTO = (ThemeDTO) target;
            ThemeDTO theme = themeService.getById(themeDTO.getId());
            if (theme == null) {
                errors.reject("id", "theme.error.not.exist");
            }
        }
    }
}
