package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class UserReviewConverterImpl implements Converter<UserDTO, User> {

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        return userDTO;
    }

    @Override
    public User fromDTO(UserDTO userDTO) {
        User user = new User();
        if (userDTO != null) {
            user.setId(userDTO.getId());
            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());
        }
        return user;
    }
}
