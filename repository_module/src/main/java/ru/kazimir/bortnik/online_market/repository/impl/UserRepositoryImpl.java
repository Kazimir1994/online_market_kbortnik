package ru.kazimir.bortnik.online_market.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.exception.UserRepositoryException;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.repository.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.ADD_USER_FILED;
import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.DELETE_USER_FAILED;
import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.ERROR_QUERY_FAILED;
import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.UPDATE_PASSWORD_FAILED;
import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.UPDATE_ROLE_FAILED;


@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public List<User> getUsers(Connection connection, Long limitPositions, Long positions) {
        String sqlQuery = "SELECT  role.name AS role," +
                " User.id,User.name,User.surname ,User.patronymic,User.password,User.email,User.canBeRemoved" +
                " FROM User JOIN Role ON Role.id=User.role_id WHERE deleted=FALSE ORDER BY email LIMIT ? OFFSET ?";
        List<User> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, limitPositions);
            preparedStatement.setLong(2, positions);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    userList.add(buildingUser(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
        return userList;
    }

    @Override
    public Long getCountOfUsers(Connection connection) {
        String sqlQuery = "SELECT COUNT(*) AS number_of_users FROM User WHERE User.deleted = FALSE";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
                if (resultSet.next()) {
                    return resultSet.getLong("number_of_users");
                }
                throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }

    @Override
    public void deleteUsersById(Connection connection, Long id) {
        String sqlQuery = "UPDATE user SET deleted=TRUE  WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            int sizeDelete = preparedStatement.executeUpdate();
            if (sizeDelete > 0) {
                logger.info("User ID = {} has been deleted", id);
            } else {
                throw new UserRepositoryException(String.format(DELETE_USER_FAILED, id));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }

    @Override
    public void updateRole(Connection connection, Long idRole, Long idUser) {
        String sqlQuery = "UPDATE user SET role_id= ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, idRole);
            preparedStatement.setLong(2, idUser);
            int sizeUpdate = preparedStatement.executeUpdate();
            if (sizeUpdate > 0) {
                logger.info("The user with id = {} has been assigned the idRole = {}", idRole, idUser);
            } else {
                throw new UserRepositoryException(String.format(UPDATE_ROLE_FAILED, idRole, idUser));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }

    @Override
    public void updatePasswordByEmail(Connection connection, String email, String password) {
        String sqlQuery = "UPDATE user SET password=? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            int sizeUpdate = preparedStatement.executeUpdate();
            if (sizeUpdate > 0) {
                logger.info("Password was successfully updated by user( Email = {} )", email);
            } else {
                throw new UserRepositoryException(String.format(UPDATE_PASSWORD_FAILED, email));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }

    @Override
    public void add(Connection connection, User user) {
        String sqlQuery = "INSERT INTO User (name,surname,patronymic,email, password, role_id) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getPatronymic());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getRole().getId());
            int sizeAdd = preparedStatement.executeUpdate();
            if (sizeAdd > 0) {
                logger.info("User = {} was added successfully", user);
            } else {
                throw new UserRepositoryException(String.format(ADD_USER_FILED, user.toString()));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }


    @Override
    public User getByEmail(Connection connection, String email) {
        String sqlQuery = "SELECT  role.name AS role, User.id, User.name, User.password" +
                " FROM User JOIN Role ON Role.id=User.role_id WHERE User.email=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return buildingUserAuthentication(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UserRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
        return null;
    }

    private User buildingUserAuthentication(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(new Role(resultSet.getString("role")));
        return user;
    }

    private User buildingUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setPatronymic(resultSet.getString("patronymic"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setCanBeRemoved(resultSet.getBoolean("canBeRemoved"));
        user.setRole(new Role(resultSet.getString("role")));
        return user;
    }
}
