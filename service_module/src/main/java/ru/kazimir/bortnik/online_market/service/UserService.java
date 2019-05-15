package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getUsers(Long limitPositions, Long positions);

    Long getNumberOfPages(Long maxPositions);

    void deleteUsersById(List<Long> listID);

    void updateRole(UserDTO userDTO);

    void updatePasswordByEmail(String email);

    void add(UserDTO userDTO);

    UserDTO getByEmail(String email);
}
