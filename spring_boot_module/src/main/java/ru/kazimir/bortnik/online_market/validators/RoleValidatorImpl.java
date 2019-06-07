package ru.kazimir.bortnik.online_market.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoleValidatorImpl implements Validator {

    private final RoleService roleService;

    @Autowired
    public RoleValidatorImpl(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RoleDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleDTO role = (RoleDTO) target;
        List<RoleDTO> roleDTOS = roleService.getRoles();
        if (roleDTOS.stream().filter(roleDTO -> roleDTO.getId().equals(role.getId())).collect(Collectors.toList()).isEmpty()) {
            errors.rejectValue("name", "role.error.not.exist");
        }
    }
}
