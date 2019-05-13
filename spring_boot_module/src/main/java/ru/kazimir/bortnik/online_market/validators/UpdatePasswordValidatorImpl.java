package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class UpdatePasswordValidatorImpl implements Validator {
    private final UserService userService;

    @Autowired
    public UpdatePasswordValidatorImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (!errors.hasFieldErrors("email")) {
            UserDTO user = userService.getByEmail(userDTO.getEmail());
            if (user == null) {
                errors.rejectValue("email", null, null);
            }
        }
    }
}
