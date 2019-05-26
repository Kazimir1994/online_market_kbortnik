package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import java.util.List;

public interface UserService {

    PageDTO<UserDTO> getUsers(Long limitPositions, Long currentPage);

    void deleteUsersById(List<Long> listID);

    void updateRole(UserDTO userDTO);

    void updatePasswordByEmail(String email);

    void add(UserDTO userDTO);

    UserDTO getByEmail(String email);

    UserDTO getUserWithProfile(Long id);

    UserDTO checkUserForExist(Long id);

    String getPasswordById(Long id);

    void updateProfile(UserDTO userDTO);
}
