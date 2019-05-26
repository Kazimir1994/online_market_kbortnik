package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.RoleRepository;
import ru.kazimir.bortnik.online_market.repository.model.Role;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Long,Role > implements RoleRepository {
}
