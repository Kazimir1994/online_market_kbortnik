package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.User;

import java.util.List;

public interface UserRepository extends GenericRepository<Long, User> {

    User getByEmail(String email);

    List<User> findAll(Long offset, Long limit);
}
