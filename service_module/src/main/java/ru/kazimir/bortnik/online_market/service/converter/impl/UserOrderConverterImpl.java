package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Profile;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ProfileDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class UserOrderConverterImpl implements Converter<UserDTO, User> {
    private final Converter<ProfileDTO, Profile> profileConverter;

    public UserOrderConverterImpl(Converter<ProfileDTO, Profile> profileConverter) {
        this.profileConverter = profileConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setProfileDTO(profileConverter.toDTO(user.getProfile()));
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO ObjectDTO) {
        throw new UnsupportedOperationException();
    }
}
