package ru.kazimir.bortnik.online_market.controllers;

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
import ru.kazimir.bortnik.online_market.model.Pageable;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.UserService;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.constant.ErrorsMessage.*;
import static ru.kazimir.bortnik.online_market.constant.URLConstants.*;

@Controller
@RequestMapping(PRIVATE_USERS_URL)
public class UsersWebController {
    private final static Logger logger = LoggerFactory.getLogger(UsersWebController.class);

    private final UserService userService;
    private final RoleService roleService;
    private final Validator addUserValidator;
    private final Validator roleValidator;
    private final Validator updatePasswordValidator;
    private final Pageable pageable = new Pageable(10L);

    @Autowired
    public UsersWebController(UserService userService,
                              RoleService roleService,
                              @Qualifier("addUserValidatorImpl") Validator addUserValidator,
                              @Qualifier("roleValidatorImpl") Validator roleValidator,
                              @Qualifier("updatePasswordValidatorImpl") Validator updatePasswordValidator) {
        this.userService = userService;
        this.roleService = roleService;
        this.addUserValidator = addUserValidator;
        this.roleValidator = roleValidator;
        this.updatePasswordValidator = updatePasswordValidator;
    }

    @GetMapping(PRIVATE_USERS_SHOWING_URL)
    public String showUsers(@RequestParam(defaultValue = "1") Long currentPage, UserDTO userDTO, Model model) {
        Long amountOfPages = userService.getNumberOfPages(pageable.getLimitPositions());
        if (currentPage > amountOfPages) {
            currentPage = amountOfPages;
        } else if (currentPage < amountOfPages) {
            currentPage = 1L;
        }
        List<UserDTO> userDTOS = userService.getUsers(pageable.getLimitPositions(), currentPage);
        logger.info("Page add Show user. list of users for submission \n{}", userDTOS);
        userDTO.setRoleDTO(new RoleDTO());
        model.addAttribute("users", userDTOS);

        List<RoleDTO> roleDTOS = roleService.getRoles();
        model.addAttribute("roles", roleDTOS);
        model.addAttribute("SizePage", amountOfPages);
        model.addAttribute("currentPage", currentPage);
        return PRIVATE_USERS_PAGE;
    }

    @PostMapping(PRIVATE_DELETE_USERS_URL)
    public String deleteUsers(
            @RequestParam(value = "id_delete_users", required = false) Long[] idDeleteUsers,
                              RedirectAttributes redirectAttributes) {
        if (idDeleteUsers != null) {
            List<Long> longList = Arrays.stream(idDeleteUsers).filter(aLong -> aLong != null && aLong > 0).collect(Collectors.toList());
            logger.info("Request to delete users by ID {}", longList);
            if (!longList.isEmpty()) {
                userService.deleteUsersById(longList);
                redirectAttributes.addFlashAttribute("message", "Selected users were successfully deleted");
            }
        }
        return REDIRECT_PRIVATE_USERS_SHOWING;
    }

    @PostMapping(PRIVATE_UPDATE_ROLE_USERS_URL)
    public String updateRole(UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("Request to update role ( User = {} )", userDTO);
        roleValidator.validate(userDTO.getRoleDTO(), bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("message", "Cannot change role with sent data.");
            logger.info("Request denied. Error code := {}", ERROR_UPDATE_ROLE);
            return REDIRECT_PRIVATE_USERS_SHOWING;
        }

        userService.updateRole(userDTO);
        redirectAttributes.addFlashAttribute("message", "Successful role changed");
        return REDIRECT_PRIVATE_USERS_SHOWING;
    }

    @PostMapping(PRIVATE_CHANGE_PASSWORD_USERS_URL)
    public String updatePassword(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        logger.info("Password change request for user ( Email = {} )", userDTO.getEmail());
        updatePasswordValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasFieldErrors("email")) {
            redirectAttributes.addFlashAttribute("message", "Cannot change password with sent data.");
            logger.info("Request denied. Error code := {}", ERROR_UPDATE_PASSWORD_USER_BY_EMAIL);
            return REDIRECT_PRIVATE_USERS_SHOWING;
        }
        userService.updatePasswordByEmail(userDTO.getEmail());
        redirectAttributes.addFlashAttribute("message", "Password has been successfully changed");
        return REDIRECT_PRIVATE_USERS_SHOWING;
    }

    @GetMapping(PRIVATE_ADD_FORM_USERS_URL)
    public String formAddUsers(UserDTO userDTO, BindingResult results, Model model) {
        List<RoleDTO> roleDTOS = roleService.getRoles();
        logger.info("Page add User .list of role for submission \n{}", roleDTOS);
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
        logger.info("Request to add a user ( User = {} )", userDTO);
        addUserValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", bindingResult.getAllErrors());
            logger.info("Request denied. Error code := {}", ERROR_ADD_USER);
            return REDIRECT_PRIVATE_FORM_ADD_USERS;
        }

        logger.info("Add user{}", userDTO);
        userService.add(userDTO);
        redirectAttributes.addFlashAttribute("message", "User was added successfully");
        return REDIRECT_PRIVATE_FORM_ADD_USERS;
    }

}
