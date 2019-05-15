package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDetail;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_EMAIL;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);
    private final UserService userService;

    @Autowired
    public AppUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("User is trying to login by Email:= {}", email);

        if (email.matches(REGEX_EMAIL)) {
            UserDTO userDTO = userService.getByEmail(email);
            if (userDTO == null) {
                logger.info("User with this email was not found {}", email);
                throw new UsernameNotFoundException("User is not found");
            }
            logger.info("User found{}", userDTO);
            return new UserDetail(userDTO);
        }
        logger.info("Not valid email {}", email);
        throw new UsernameNotFoundException("Not valid email");
    }
}
