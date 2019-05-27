package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.RoleRepository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.GenerationEncodePassword;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.UserServiceException;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_PASSWORD;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_PROFILE;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_USERS;
import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_UPDATE_PROFILE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.ADD_USER_ERROR_MESSAGE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_DELETE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_GET_USER;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_UPDATE_PASSWORD;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.USER_ERROR_UPDATE_ROLE;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Converter<UserDTO, User> userConverter;
    private final Converter<UserDTO, User> userProfileConverter;
    private final Converter<UserDTO, User> checkUserForExistConverter;
    private final Converter<UserDTO, User> userAddConverter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GenerationEncodePassword generationEncodePassword;

    @Autowired
    public UserServiceImpl(@Qualifier("userConverterImpl") Converter<UserDTO, User> userConverter,
                           @Qualifier("userProfileConverterImpl") Converter<UserDTO, User> userProfileConverter,
                           @Qualifier("checkUserForExistConverterImpl") Converter<UserDTO, User> checkUserForExistConverter,
                           @Qualifier("userSaveConverterImpl") Converter<UserDTO, User> userAddConverter, UserRepository userRepository,
                           RoleRepository roleRepository, GenerationEncodePassword generationEncodePassword) {
        this.userConverter = userConverter;
        this.userProfileConverter = userProfileConverter;
        this.checkUserForExistConverter = checkUserForExistConverter;
        this.userAddConverter = userAddConverter;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.generationEncodePassword = generationEncodePassword;
    }

    @Transactional
    @Override
    public PageDTO<UserDTO> getUsers(Long limitPositions, Long currentPage) {
        PageDTO<UserDTO> pageDTO = new PageDTO<>();
        Long countOfUsers = userRepository.getCountOfEntities();
        Long countOfPages = calculationCountOfPages(countOfUsers, limitPositions);
        pageDTO.setCountOfPages(countOfPages);

        if (currentPage > countOfPages) {
            currentPage = countOfPages;
        } else if (currentPage < countOfPages) {
            currentPage = 1L;
        }
        Long offset = getPosition(limitPositions, currentPage);
        logger.info(MESSAGES_GET_USERS, limitPositions, offset);
        List<User> userList = userRepository.findAll(offset, limitPositions);
        List<UserDTO> userDTOList = userList.stream().map(userConverter::toDTO).collect(Collectors.toList());
        pageDTO.setList(userDTOList);
        return pageDTO;
    }

    @Transactional
    @Override
    public void deleteUsersById(List<Long> listId) {
        listId.forEach(id -> {
            User user = userRepository.findById(id);
            if (user != null) {
                userRepository.remove(user);
            } else {
                throw new UserServiceException(String.format(USER_ERROR_DELETE, listId.stream()
                        .map(aLong -> "(" + aLong + ")")
                        .collect(joining(","))));
            }
        });
    }

    @Transactional
    @Override
    public void updateRole(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getId());
        if (user != null) {
            Role role = roleRepository.findById(userDTO.getRoleDTO().getId());
            user.setRole(role);
            userRepository.merge(user);
        } else {
            throw new UserServiceException(String.format(USER_ERROR_UPDATE_ROLE,
                    userDTO.getRoleDTO().getId(), userDTO.getId()));
        }
    }

    @Transactional
    @Override
    public void updatePasswordByEmail(String email) {
        User user = userRepository.getByEmail(email);
        if (user != null) {
            String password = generationEncodePassword.getPassword();
            user.setPassword(password);
            userRepository.merge(user);
        } else {
            throw new UserServiceException(String.format(USER_ERROR_UPDATE_PASSWORD, email));
        }
    }

    @Transactional
    @Override
    public void add(UserDTO userDTO) {
        try {
            String password = generationEncodePassword.getPassword();
            userDTO.setPassword(password);
            User user = userAddConverter.fromDTO(userDTO);
            user.getProfile().setUser(user);
            userRepository.persist(user);
        } catch (Exception e) {
            throw new UserServiceException(String.format(ADD_USER_ERROR_MESSAGE, userDTO));
        }
    }

    @Transactional
    @Override
    public UserDTO getByEmail(String email) {
        User user = userRepository.getByEmail(email);
        if (user == null) {
            return null;
        }
        return userConverter.toDTO(user);
    }

    @Transactional
    @Override
    public UserDTO getUserWithProfile(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            logger.info(MESSAGES_GET_PROFILE, id, user);
            return userProfileConverter.toDTO(user);
        } else {
            logger.info(USER_ERROR_GET_USER, id);
            throw new UserServiceException(String.format(USER_ERROR_GET_USER, id));
        }
    }

    @Transactional
    @Override
    public UserDTO checkUserForExist(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            return checkUserForExistConverter.toDTO(user);
        } else {
            logger.info(USER_ERROR_GET_USER, id);
            throw new UserServiceException();
        }
    }

    @Transactional
    @Override
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

    @Transactional
    @Override
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

    private Long calculationCountOfPages(Long countOfArticle, Long limitPositions) {
        Long countOfPages;
        if (countOfArticle % limitPositions > 0) {
            countOfPages = (countOfArticle / limitPositions) + 1;
        } else {
            countOfPages = countOfArticle / limitPositions;
        }
        return countOfPages;
    }

}
