package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public User getByEmail(String email) {
        String queryString = "from " + User.class.getName() + " where email =:email ";
        Query query = entityManager.createQuery(queryString).setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
