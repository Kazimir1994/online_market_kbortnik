package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.RoleRepository;
import ru.kazimir.bortnik.online_market.repository.model.Role;
import ru.kazimir.bortnik.online_market.service.RoleService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.RoleDTO;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    @Override
    public List<RoleDTO> getRoles() {
        List<Role> roleList = roleRepository.findAll();
        logger.info("Got the following list of roles := {} ", roleList);
        return roleList.stream().map(roleConverter::toDTO).collect(Collectors.toList());
    }
}
