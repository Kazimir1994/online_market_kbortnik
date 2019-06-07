package ru.kazimir.bortnik.online_market.controllers.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;

import java.util.List;

import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ROLE_SHOWING_URL;
import static ru.kazimir.bortnik.online_market.constant.ApiURLConstants.API_ROLE_URL;

@RestController
@RequestMapping(API_ROLE_URL)
public class RoleAPIController {
    private final static Logger logger = LoggerFactory.getLogger(ItemAPIController.class);

    private final RoleService roleService;

    public RoleAPIController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping(API_ROLE_SHOWING_URL)
    public ResponseEntity<List<RoleDTO>> getRoles() {
        logger.info("Request for receiving roles");
        List<RoleDTO> roles = roleService.getRoles();
        logger.info("Send a list of roles. := {}.", roles);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
