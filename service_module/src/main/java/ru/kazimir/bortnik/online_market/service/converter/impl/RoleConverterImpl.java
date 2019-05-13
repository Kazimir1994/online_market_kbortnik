package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;

@Component
public class RoleConverterImpl implements Converter<RoleDTO, Role> {

    @Override
    public RoleDTO toDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(role.getName());
        return roleDTO;
    }

    @Override
    public Role fromDTO(RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        return role;
    }
}
