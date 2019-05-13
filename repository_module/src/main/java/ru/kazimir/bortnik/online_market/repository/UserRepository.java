package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.User;

import java.sql.Connection;
import java.util.List;

public interface UserRepository extends GenericRepository {
    List<User> getUsers(Connection connection, Long limitPositions, Long positions);

    Long getSizeData(Connection connection);

    void deleteUsersById(Connection connection, Long id);

    void updateRole(Connection connection, String role, Long id);

    void updatePasswordByEmail(Connection connection, String email, String password);

    void add(Connection connection, User user);

    User getByEmail(Connection connection, String email);
}
