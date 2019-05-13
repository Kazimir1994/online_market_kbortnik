package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kazimir.bortnik.online_market.repository.RoleRepository;
import ru.kazimir.bortnik.online_market.repository.exception.ConnectionDataBaseExceptions;
import ru.kazimir.bortnik.online_market.repository.exception.UserRepositoryException;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.RoleServiceException;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.NO_CONNECTION;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ROLE_ERROR_MESSAGE;

@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;
    private final Converter<RoleDTO, Role> roleConverter;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, Converter<RoleDTO, Role> roleConverter) {
        this.roleRepository = roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    public List<RoleDTO> getRoles() {
        try (Connection connection = roleRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<Role> roleList = roleRepository.getRoles(connection);
                List<RoleDTO> roleDTOList = roleList.stream().map(roleConverter::toDTO).collect(Collectors.toList());
                connection.commit();
                return roleDTOList;
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new RoleServiceException(ROLE_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(NO_CONNECTION, e);
        }
    }
}
