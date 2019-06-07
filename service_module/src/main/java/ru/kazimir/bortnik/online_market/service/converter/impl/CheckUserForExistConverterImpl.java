package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class CheckUserForExistConverterImpl implements Converter<UserDTO, User> {
    private final Converter<RoleDTO, Role> roleConverter;

    @Autowired
    public CheckUserForExistConverterImpl(Converter<RoleDTO, Role> roleConverter) {
        this.roleConverter = roleConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        RoleDTO roleDTO = roleConverter.toDTO(user.getRole());
        userDTO.setRoleDTO(roleDTO);
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO ObjectDTO) {
        throw new UnsupportedOperationException();
    }
}
