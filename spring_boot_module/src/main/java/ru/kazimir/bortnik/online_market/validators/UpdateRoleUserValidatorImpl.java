package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.exception.UserServiceException;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class UpdateRoleUserValidatorImpl implements Validator {

    private final UserService userService;
    private final Validator roleValidator;

    @Autowired
    public UpdateRoleUserValidatorImpl(UserService userService, @Qualifier("roleValidatorImpl") Validator roleValidator) {
        this.userService = userService;
        this.roleValidator = roleValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            UserDTO userDTO = (UserDTO) target;
            try {
                roleValidator.validate(userDTO.getRoleDTO(), errors);
                if (!errors.hasErrors()) {
                    UserDTO user = userService.checkUserForExist(userDTO.getId());
                    if (user.getRoleDTO().getId().equals(userDTO.getRoleDTO().getId())) {
                        errors.rejectValue("roleDTO", null, null);
                    }
                }
            } catch (UserServiceException e) {
                errors.rejectValue("id", "user.error.not.exist");
            }
        }
    }
}