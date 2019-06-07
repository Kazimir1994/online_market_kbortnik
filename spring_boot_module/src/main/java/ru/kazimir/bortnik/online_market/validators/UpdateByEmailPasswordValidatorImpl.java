package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_EMAIL;

@Component
public class UpdateByEmailPasswordValidatorImpl implements Validator {

    private final UserService userService;

    @Autowired
    public UpdateByEmailPasswordValidatorImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userDTO.getEmail().length() < 40) {
            if (!userDTO.getEmail().matches(REGEX_EMAIL)) {
                errors.rejectValue("email", "user.error.user.email");
            }
        } else {
            errors.rejectValue("email", "user.error.email.size");
        }
        if (!errors.hasFieldErrors("email")) {
            UserDTO user = userService.getByEmail(userDTO.getEmail());
            if (user == null) {
                errors.rejectValue("email", "user.error.user.email");
            }
        }
    }
}
