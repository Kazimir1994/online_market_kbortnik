package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_EMAIL;
import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_PATRONYMIC;

@Component
public class SaveUserValidatorImpl implements Validator {
    private final UserService userService;
    private final Validator roleValidator;

    @Autowired
    public SaveUserValidatorImpl(UserService userService,
                                 @Qualifier("roleValidatorImpl") Validator roleValidator) {
        this.userService = userService;
        this.roleValidator = roleValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userDTO.getPatronymic() != null) {
            if (userDTO.getPatronymic().length() < 40) {
                if (!userDTO.getPatronymic().matches(REGEX_PATRONYMIC)) {
                    errors.rejectValue("patronymic", "user.error.user.patronymic");
                }
            } else {
                errors.rejectValue("patronymic", "user.error.patronymic.size");
            }
        } else {
            errors.rejectValue("patronymic", "user.error.user.patronymic");
        }
        if (userDTO.getEmail() != null) {
            if (userDTO.getEmail().length() < 40) {
                if (!userDTO.getEmail().matches(REGEX_EMAIL)) {
                    errors.rejectValue("email", "user.error.user.email");
                }
            } else {
                errors.rejectValue("email", "user.error.email.size");
            }
        } else {
            errors.rejectValue("email", "user.error.user.email");
        }
        if (!errors.hasFieldErrors()) {
            UserDTO user = userService.getByEmail(userDTO.getEmail());
            if (user != null) {
                errors.rejectValue("email",
                        "user.error.user.Add.email",
                        "User with the same name already exists.");
            } else {
                roleValidator.validate(userDTO.getRoleDTO(), errors);
            }
        }
    }
}
