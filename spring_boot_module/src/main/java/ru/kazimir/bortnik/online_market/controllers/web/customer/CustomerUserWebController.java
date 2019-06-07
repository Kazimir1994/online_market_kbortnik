package ru.kazimir.bortnik.online_market.controllers.web.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDetail;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_GET_UNAUTHORIZED_USER;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_UNAUTHORIZED_USER;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_UPDATE_PROFILE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_PROFILE_USER_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_REDIRECT_SHOW_PROFILE_USER;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_SAIL_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_SHOW_PROFILE_USER_URl;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PUBLIC_CUSTOMER_UPRATE_PROFILE_USER_URl;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_LOGIN_URL;

@Controller
@RequestMapping(PUBLIC_CUSTOMER_SAIL_URL)
public class CustomerUserWebController {
    private final static Logger logger = LoggerFactory.getLogger(CustomerUserWebController.class);
    private final UserService userService;
    private final Validator updateProfileUserValidator;

    @Autowired
    public CustomerUserWebController(UserService userServiceImpl,
                                     @Qualifier("updateProfileUserValidatorImpl") Validator updateProfileUserValidatorImpl) {
        this.userService = userServiceImpl;

        this.updateProfileUserValidator = updateProfileUserValidatorImpl;
    }

    @GetMapping(PUBLIC_CUSTOMER_SHOW_PROFILE_USER_URl)
    public String showProfileUser(Authentication authentication, UserDTO user, BindingResult results, Model model) {
        Long idUser;
        try {
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            logger.info("Request to display profile user := {}.", userDetail);
            if (Objects.isNull(idUser = userDetail.getUserDTO().getId())) {
                return REDIRECT_LOGIN_URL;
            }
        } catch (NullPointerException e) {
            logger.error("Request denied. Error code := {}.", ERROR_UNAUTHORIZED_USER);
            logger.error(e.getMessage(), e);
            return REDIRECT_LOGIN_URL;
        }
        UserDTO userDTO = userService.getUserWithProfile(idUser);
        logger.info("Profile to display on the page := {}.", userDTO);
        model.addAttribute("Profile", userDTO);

        if (model.containsAttribute("error")) {
            for (ObjectError error : ((List<ObjectError>) model.asMap().get("error"))) {
                results.addError(error);
            }
        }
        return PUBLIC_CUSTOMER_PROFILE_USER_PAGE;
    }

    @PostMapping(PUBLIC_CUSTOMER_UPRATE_PROFILE_USER_URl)
    public String updateProfile(Authentication authentication, @Valid UserDTO userDTO,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            UserDetail userDetail = (UserDetail) authentication.getPrincipal();
            logger.info("Get user out of session := {}.", userDetail);
            Long idUser;
            if (Objects.isNull(idUser = userDetail.getUserDTO().getId())) {
                return REDIRECT_LOGIN_URL;
            }
            userDTO.setId(idUser);
        } catch (NullPointerException e) {
            logger.error("Request denied. Error code := {}.", ERROR_GET_UNAUTHORIZED_USER);
            logger.error(e.getMessage(), e);
            return REDIRECT_LOGIN_URL;
        }
        logger.info("Profile change request := {}.", userDTO);
        updateProfileUserValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
            logger.info("Request denied. Error code := {},{}", ERROR_UPDATE_PROFILE, bindingResult.getAllErrors());
            return PUBLIC_CUSTOMER_REDIRECT_SHOW_PROFILE_USER;
        }
        userService.updateProfile(userDTO);
        redirectAttributes.addFlashAttribute("message", "Profile was successfully changed");
        return PUBLIC_CUSTOMER_REDIRECT_SHOW_PROFILE_USER;
    }
}
