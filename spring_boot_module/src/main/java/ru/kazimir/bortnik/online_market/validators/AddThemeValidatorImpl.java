package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.ThemeService;
import ru.kazimir.bortnik.online_market.service.model.ThemeDTO;

@Component
public class AddThemeValidatorImpl implements Validator {

    private final ThemeService themeService;

    @Autowired
    public AddThemeValidatorImpl(ThemeService themeService) {
        this.themeService = themeService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ThemeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ThemeDTO themeDTO = (ThemeDTO) target;
        if (themeDTO.getName() != null) {
            if (themeDTO.getName().length() < 40 && themeDTO.getName().length() > 5) {
                ThemeDTO theme = themeService.getByName(themeDTO.getName());
                if (theme != null) {
                    errors.reject("name", "theme.error.exist");
                }
            } else {
                errors.reject("name", "theme.error.name.size");
            }
        } else {
            errors.reject("name", "theme.error.null");
        }
    }
}
