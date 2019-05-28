package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Profile;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ProfileDTO;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class UserSaveConverterImpl implements Converter<UserDTO, User> {
    private final Converter<RoleDTO, Role> roleConverter;
    private final Converter<ProfileDTO, Profile> profileConverter;

    @Autowired
    public UserSaveConverterImpl(Converter<RoleDTO, Role> roleConverter, Converter<ProfileDTO, Profile> profileConverter) {
        this.roleConverter = roleConverter;
        this.profileConverter = profileConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRole(roleConverter.fromDTO(userDTO.getRoleDTO()));
        user.setProfile(profileConverter.fromDTO(userDTO.getProfileDTO()));
        return user;
    }
}