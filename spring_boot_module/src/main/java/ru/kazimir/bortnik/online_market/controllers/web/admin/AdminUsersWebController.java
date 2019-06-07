package ru.kazimir.bortnik.online_market.controllers.web.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.exception.UserServiceException;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.Pageable;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_ADD_USER;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_UPDATE_PASSWORD_USER_BY_EMAIL;
import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.ERROR_UPDATE_ROLE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_ADD_FORM_USERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_ADD_USERS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_ADD_USERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_CHANGE_PASSWORD_USERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_DELETE_USERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_UPDATE_ROLE_USERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_USERS_PAGE;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_USERS_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.PRIVATE_USERS_URL;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_404;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_ERROR_422;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_PRIVATE_FORM_ADD_USERS;
import static ru.kazimir.bortnik.online_market.constant.WebURLConstants.REDIRECT_PRIVATE_USERS_SHOWING;

@Controller
@RequestMapping(PRIVATE_USERS_URL)
public class AdminUsersWebController {
    private final static Logger logger = LoggerFactory.getLogger(AdminUsersWebController.class);
    private final UserService userService;
    private final RoleService roleService;
    private final Validator addUserValidator;
    private final Validator updateRoleUserValidator;
    private final Validator updateByEmailPasswordValidator;
    private final Pageable pageable = new Pageable(10L);

    @Autowired
    public AdminUsersWebController(UserService userServiceImpl,
                                   RoleService roleServiceImpl,
                                   @Qualifier("addUserValidatorImpl") Validator addUserValidatorImpl,
                                   @Qualifier("updateRoleUserValidatorImpl") Validator updateRoleUserValidatorImpl,
                                   @Qualifier("updateByEmailPasswordValidatorImpl") Validator updatePasswordValidatorImpl) {
        this.userService = userServiceImpl;
        this.roleService = roleServiceImpl;
        this.addUserValidator = addUserValidatorImpl;
        this.updateRoleUserValidator = updateRoleUserValidatorImpl;
        this.updateByEmailPasswordValidator = updatePasswordValidatorImpl;
    }

    @GetMapping(PRIVATE_USERS_SHOWING_URL)
    public String showUsers(@RequestParam(defaultValue = "1") Long currentPage, UserDTO userDTO, Model model) {
        PageDTO<UserDTO> userDTOS = userService.getUsers(pageable.getLimitPositions(), currentPage);
        logger.info("List of User for showing := {}", userDTOS.getList());
        List<RoleDTO> roleDTOS = roleService.getRoles();
        logger.info("List of Roles for showing := {}", userDTOS.getList());
        userDTO.setRoleDTO(new RoleDTO());
        model.addAttribute("roles", roleDTOS);
        model.addAttribute("users", userDTOS);
        return PRIVATE_USERS_PAGE;
    }

    @PostMapping(PRIVATE_DELETE_USERS_URL)
    public String deleteUsers(
            @RequestParam(value = "id_delete_users", required = false) Long[] idDeleteUsers,
            RedirectAttributes redirectAttributes) {
        if (idDeleteUsers != null) {
            List<Long> longList = Arrays.stream(idDeleteUsers).filter(aLong -> aLong != null && aLong > 0)
                    .collect(Collectors.toList());
            logger.info("Request to delete users by ID {}.", longList);
            if (!longList.isEmpty()) {
                try {
                    userService.deleteUsersById(longList);
                    redirectAttributes.addFlashAttribute("message",
                            "Selected users were successfully deleted");
                } catch (UserServiceException e) {
                    return REDIRECT_ERROR_404;
                }
            }
        }
        return REDIRECT_PRIVATE_USERS_SHOWING;
    }

    @PostMapping(PRIVATE_UPDATE_ROLE_USERS_URL)
    public String updateRole(UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("Request to update role ( User := {}. )", userDTO);
        updateRoleUserValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("roleDTO")) {
                redirectAttributes.addFlashAttribute("message", "The role has not been changed");
                return REDIRECT_PRIVATE_USERS_SHOWING;
            } else {
                logger.info("Request denied. Error code := {},{}.", ERROR_UPDATE_ROLE, bindingResult.getAllErrors());
                return REDIRECT_ERROR_422;
            }
        }
        try {
            userService.updateRole(userDTO);
            redirectAttributes.addFlashAttribute("message", "Successful role changed");
        } catch (UserServiceException e) {
            return REDIRECT_ERROR_404;
        }
        return REDIRECT_PRIVATE_USERS_SHOWING;
    }

    @PostMapping(PRIVATE_CHANGE_PASSWORD_USERS_URL)
    public String updatePassword(UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("Password change request for user ( Email := {} )", userDTO.getEmail());
        updateByEmailPasswordValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasFieldErrors("email")) {
            logger.info("Request denied. Error code := {},{}.", ERROR_UPDATE_PASSWORD_USER_BY_EMAIL,
                    bindingResult.getAllErrors());
            return REDIRECT_ERROR_422;
        }
        try {
            userService.updatePasswordByEmail(userDTO.getEmail());
            redirectAttributes.addFlashAttribute("message", "Password has been successfully changed");
            return REDIRECT_PRIVATE_USERS_SHOWING;
        } catch (UserServiceException e) {
            return REDIRECT_ERROR_404;
        }

    }

    @GetMapping(PRIVATE_ADD_FORM_USERS_URL)
    public String formAddUsers(UserDTO userDTO, BindingResult results, Model model) {
        List<RoleDTO> roleDTOS = roleService.getRoles();
        logger.info("List of Roles for showing := {}", roleDTOS);
        userDTO.setRoleDTO(new RoleDTO());
        model.addAttribute("roles", roleDTOS);

        if (model.containsAttribute("error")) {
            for (ObjectError error : ((List<ObjectError>) model.asMap().get("error"))) {
                results.addError(error);
            }
        }
        return PRIVATE_ADD_USERS_PAGE;
    }

    @PostMapping(PRIVATE_ADD_USERS_URL)
    public String addUsers(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("Request to add a user ( User := {}. )", userDTO);
        addUserValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
            logger.info("Request denied. Error code := {},{}.", ERROR_ADD_USER, bindingResult.getAllErrors());
            return REDIRECT_PRIVATE_FORM_ADD_USERS;
        }

        userService.add(userDTO);
        redirectAttributes.addFlashAttribute("message", "User was added successfully");
        return REDIRECT_PRIVATE_FORM_ADD_USERS;
    }

}
