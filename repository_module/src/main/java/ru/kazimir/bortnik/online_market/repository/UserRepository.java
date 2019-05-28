package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.User;

public interface UserRepository extends GenericRepository<Long, User> {
    User getByEmail(String email);
}
