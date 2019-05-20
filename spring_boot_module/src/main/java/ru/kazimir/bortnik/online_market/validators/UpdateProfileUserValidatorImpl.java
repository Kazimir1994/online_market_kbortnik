package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.GenerationEncodePassword;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.DEFAULT_SIZE_OF_PASSWORDS;

@Component
public class UpdateProfileUserValidatorImpl implements Validator {
    private final UserService userService;
    private final Validator profileValidator;
    private final GenerationEncodePassword generationEncodePassword;

    public UpdateProfileUserValidatorImpl(UserService userService,
                                          @Qualifier("profileValidatorImpl") Validator profileValidator,
                                          GenerationEncodePassword generationEncodePassword) {
        this.userService = userService;
        this.profileValidator = profileValidator;
        this.generationEncodePassword = generationEncodePassword;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userDTO.getProfileDTO() != null) {
            profileValidator.validate(userDTO.getProfileDTO(), errors);
        } else {
            errors.rejectValue("profileDTO", "profile.error.null");
        }
        if (!errors.hasErrors()) {
            String[] passwords = userDTO.getPassword().split(",");
            switch (passwords.length) {
                case 0: {
                    userDTO.setPassword("");
                    break;
                }
                case 1: {
                    errors.rejectValue("password", "user.error.update.password");
                    break;
                }
                case 2: {
                    if (passwords[0].length() < DEFAULT_SIZE_OF_PASSWORDS) {
                        errors.rejectValue("password", "user.error.update.new.password");
                    } else {
                        if (passwords[0].equals(passwords[1])) {
                            errors.rejectValue("password", "user.error.update.password.cannot.match.new");
                        } else {
                            String password = userService.getPasswordById(userDTO.getId());
                            if (!generationEncodePassword.comparePassword(passwords[1], password)) {
                                errors.rejectValue("password", "user.error.update.password.do.not.match");
                            } else {
                                userDTO.setPassword(passwords[1]);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
