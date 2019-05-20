package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Profile;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ProfileDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class UserProfileConverterImpl implements Converter<UserDTO, User> {
    private final Converter<ProfileDTO, Profile> profileConverter;

    @Autowired
    public UserProfileConverterImpl(Converter<ProfileDTO, Profile> profileConverter) {
        this.profileConverter = profileConverter;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        ProfileDTO profileDTO = profileConverter.toDTO(user.getProfile());
        userDTO.setProfileDTO(profileDTO);
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(user.getId());
        user.setName(user.getName());
        user.setPatronymic(user.getPatronymic());
        Profile profile = profileConverter.fromDTO(userDTO.getProfileDTO());
        user.setProfile(profile);
        return user;
    }
}
