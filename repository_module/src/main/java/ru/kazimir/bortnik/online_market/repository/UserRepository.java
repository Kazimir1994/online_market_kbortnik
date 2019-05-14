package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository {

    List<User> getUsers(Connection connection, Long limitPositions, Long positions);

    Long getCountOfUsers(Connection connection);

    void deleteUsersById(Connection connection, Long id);

    void updateRole(Connection connection, Long idRole, Long idUser);

    void updatePasswordByEmail(Connection connection, String email, String password);

    void add(Connection connection, User user);

    User getByEmail(Connection connection, String email);
}
