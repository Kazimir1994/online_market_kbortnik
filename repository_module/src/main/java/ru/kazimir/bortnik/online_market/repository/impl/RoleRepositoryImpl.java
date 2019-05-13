package ru.kazimir.bortnik.online_market.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.RoleRepository;
import ru.kazimir.bortnik.online_market.repository.exception.RoleRepositoryException;
import ru.kazimir.bortnik.online_market.repository.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.ERROR_QUERY_FAILED;


@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl implements RoleRepository {
    private static final Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Override
    public List<Role> getRoles(Connection connection) {
        String sqlQuery = "SELECT Role.name FROM  Role";
        List<Role> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    userList.add(new Role(resultSet.getString("name")));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RoleRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
        return userList;
    }

}