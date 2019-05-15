package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class AddUserValidatorImpl implements Validator {
    private final UserService userService;
    private final Validator roleValidator;

    @Autowired
    public AddUserValidatorImpl(UserService userService,
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
