package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class UserConverterImpl implements Converter<UserDTO, User> {
    private final Converter<RoleDTO, Role> roleConverter;

    @Autowired
    public UserConverterImpl(Converter<RoleDTO, Role> roleConverter) {
        this.roleConverter = roleConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setPatronymic(user.getPatronymic());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setCanBeRemoved(user.isCanBeRemoved());
        userDTO.setRoleDTO(roleConverter.toDTO(user.getRole()));
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setPatronymic(userDTO.getPatronymic());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setCanBeRemoved(userDTO.isCanBeRemoved());
        user.setRole(roleConverter.fromDTO(userDTO.getRoleDTO()));
        return user;
    }
}
