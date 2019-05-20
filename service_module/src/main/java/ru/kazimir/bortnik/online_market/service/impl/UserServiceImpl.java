package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.exception.ConnectionDataBaseExceptions;
import ru.kazimir.bortnik.online_market.repository.exception.UserRepositoryException;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.GenerationEncodePassword;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.UserServiceException;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_PASSWORD;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_PROFILE;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_UPDATE_PROFILE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ADD_USER_ERROR_MESSAGE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.CONNECTION_ERROR_MESSAGE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.NUMBER_OF_PAGES;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USERS_GET_ERROR_MESSAGE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_BY_EMAIL_MESSAGE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_DELETE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_GET_USER;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_UPDATE_PASSWORD;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_UPDATE_ROLE;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Converter<UserDTO, User> userConverter;
    private final Converter<UserDTO, User> userProfileConverter;
    private final UserRepository userRepository;
    private final GenerationEncodePassword generationEncodePassword;

    @Autowired
    public UserServiceImpl(@Qualifier("userConverterImpl") Converter<UserDTO, User> userConverter,
                           @Qualifier("userProfileConverterImpl") Converter<UserDTO, User> userProfileConverter,
                           UserRepository userRepository,
                           GenerationEncodePassword generationEncodePassword) {
        this.userConverter = userConverter;
        this.userProfileConverter = userProfileConverter;
        this.userRepository = userRepository;
        this.generationEncodePassword = generationEncodePassword;
    }

    @Override
    public List<UserDTO> getUsers(Long limitPositions, Long positions) {
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<User> userList = userRepository.getUsers(connection, limitPositions,
                        getPosition(limitPositions, positions));
                List<UserDTO> userDTOList = userList.stream().map(userConverter::toDTO).collect(Collectors.toList());
                connection.commit();
                return userDTOList;
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(USERS_GET_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public Long getNumberOfPages(Long maxPositions) {
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Long countOfUsers = userRepository.getCountOfUsers(connection);
                connection.commit();
                return calculationCountOfPages(countOfUsers, maxPositions);
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(NUMBER_OF_PAGES, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void deleteUsersById(List<Long> listId) {
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                listId.forEach(aLong -> userRepository.deleteUsersById(connection, aLong));
                connection.commit();
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(String.format(USER_ERROR_DELETE, listId.stream()
                        .map(aLong -> "(" + aLong + ")")
                        .collect(joining(","))), e);

            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void updateRole(UserDTO userDTO) {
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                userRepository.updateRole(connection, userDTO.getRoleDTO().getId(), userDTO.getId());
                connection.commit();
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(String.format(USER_ERROR_UPDATE_ROLE,
                        userDTO.getRoleDTO().getName(),
                        userDTO.getId()), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void updatePasswordByEmail(String email) {
        String password = generationEncodePassword.getPassword();
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                userRepository.updatePasswordByEmail(connection, email, password);
                connection.commit();
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(String.format(USER_ERROR_UPDATE_PASSWORD, email), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void add(UserDTO userDTO) {
        String password = generationEncodePassword.getPassword();
        userDTO.setPassword(password);
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                User user = userConverter.fromDTO(userDTO);
                userRepository.add(connection, user);
                connection.commit();
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(String.format(ADD_USER_ERROR_MESSAGE,
                        userDTO), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public UserDTO getByEmail(String email) {
        try (Connection connection = userRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                User user = userRepository.getByEmail(connection, email);
                if (user == null) {
                    return null;
                }
                UserDTO userDTO = userConverter.toDTO(user);
                connection.commit();
                return userDTO;
            } catch (SQLException | UserRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new UserServiceException(String.format(USER_ERROR_BY_EMAIL_MESSAGE, email), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    @Transactional
    public UserDTO getUserWithProfile(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            logger.info(MESSAGES_GET_PROFILE, id, user);
            return userProfileConverter.toDTO(user);
        } else {
            logger.error(USER_ERROR_GET_USER, id);
            throw new UserServiceException(String.format(USER_ERROR_GET_USER, id));
        }
    }

    @Override
    @Transactional
    public String getPasswordById(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            String password = user.getPassword();
            logger.info(MESSAGES_GET_PASSWORD, id, password);
            return password;
        } else {
            logger.error(USER_ERROR_GET_USER, id);
            throw new UserServiceException(String.format(USER_ERROR_GET_USER, id));
        }
    }

    @Override
    @Transactional
    public void updateProfile(UserDTO userDTO) {
        logger.info(MESSAGES_UPDATE_PROFILE, userDTO);
        User user = userRepository.findById(userDTO.getId());
        if (user != null) {
            user.setName(userDTO.getName());
            user.setSurname(userDTO.getSurname());
            user.getProfile().setTelephone(userDTO.getProfileDTO().getTelephone());
            user.getProfile().setResidentialAddress(userDTO.getProfileDTO().getResidentialAddress());
            if (!userDTO.getPassword().isEmpty()) {
                String newPassword = generationEncodePassword.encryptPassword(userDTO.getPassword());
                user.setPassword(newPassword);
            }
            userRepository.merge(user);
        } else {
            logger.error(USER_ERROR_GET_USER, userDTO.getId());
            throw new UserServiceException(String.format(USER_ERROR_GET_USER, userDTO.getId()));
        }
    }

    private Long getPosition(Long limitPositions, Long positions) {
        if (positions == 0) {
            positions++;
        }
        return limitPositions * (positions - 1);
    }

    private Long calculationCountOfPages(Long countOfUsers, Long maxPositions) {
        Long countOfPages;
        if (countOfUsers % maxPositions > 0) {
            countOfPages = (countOfUsers / maxPositions) + 1;
        } else {
            countOfPages = countOfUsers / maxPositions;
        }
        return countOfPages;
    }
}
