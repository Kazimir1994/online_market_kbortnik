package ru.kazimir.bortnik.online_market.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_USER_SAVE_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_USER_USER_URL;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_ADD_USER;

@RestController
@RequestMapping(API_USER_USER_URL)
public class UserAPIController {
    private final static Logger logger = LoggerFactory.getLogger(UserAPIController.class);
    private final Validator saveUserValidator;
    private final UserService userService;

    @Autowired
    public UserAPIController(@Qualifier("saveUserValidatorImpl") Validator saveUserValidator, UserService userService) {
        this.saveUserValidator = saveUserValidator;
        this.userService = userService;
    }

    @PostMapping(API_USER_SAVE_URL)
    public ResponseEntity saveUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        logger.info("Request API to add a user ( User := {}. )", userDTO);
        saveUserValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            logger.info("Request denied. Error code := {},{}.", ERROR_ADD_USER, bindingResult.getAllErrors());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        userService.add(userDTO);
        return new ResponseEntity(HttpStatus.CREATED);
    }
}
