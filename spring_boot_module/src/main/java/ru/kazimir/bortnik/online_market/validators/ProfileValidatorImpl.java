package ru.kazimir.bortnik.online_market.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.model.ProfileDTO;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_TELEPHONE;

@Component
public class ProfileValidatorImpl implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ProfileDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ProfileDTO profileDTO = (ProfileDTO) target;
            if (!profileDTO.getTelephone().matches(REGEX_TELEPHONE)) {
                errors.rejectValue("profileDTO.telephone", "profile.error.telephone");
            }
            if (profileDTO.getResidentialAddress().length() > 40 || profileDTO.getResidentialAddress().length() < 5) {
                errors.rejectValue("profileDTO.residentialAddress", "profile.error.residential.address");
            }
    }
}
