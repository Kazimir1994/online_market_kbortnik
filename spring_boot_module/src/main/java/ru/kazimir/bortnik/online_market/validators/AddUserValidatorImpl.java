package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_EMAIL;

@Component
public class AddUserValidatorImpl implements Validator {
    private final UserService userService;
    private final Validator roleValidator;
    private final Validator profileValidator;

    @Autowired
    public AddUserValidatorImpl(UserService userService,
                                @Qualifier("roleValidatorImpl") Validator roleValidator,
                                @Qualifier("profileValidatorImpl") Validator profileValidator) {
        this.userService = userService;
        this.roleValidator = roleValidator;

        this.profileValidator = profileValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
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
        if (userDTO.getProfileDTO() != null) {
            profileValidator.validate(userDTO.getProfileDTO(), errors);
        } else {
            errors.rejectValue("ProfileDTO", "profile.error.null");
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
