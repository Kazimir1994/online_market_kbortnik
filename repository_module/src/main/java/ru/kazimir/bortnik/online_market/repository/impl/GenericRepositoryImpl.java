package ru.kazimir.bortnik.online_market.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.kazimir.bortnik.online_market.repository.GenericRepository;
import ru.kazimir.bortnik.online_market.repository.exception.ConnectionDataBaseExceptions;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.NO_CONNECTION;

public class GenericRepositoryImpl implements GenericRepository {
    private static Logger logger = LoggerFactory.getLogger(GenericRepositoryImpl.class);

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(NO_CONNECTION, e);
        }
    }
}
