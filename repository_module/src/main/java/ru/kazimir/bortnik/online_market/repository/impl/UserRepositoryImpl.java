package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.model.User;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public User getByEmail(String email) {
        String queryString = "from " + User.class.getName() + " where email =:email and deleted = 0 ";
        Query query = entityManager.createQuery(queryString).setParameter("email", email);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<User> findAll(Long offset, Long limit) {
        String query = "from " + User.class.getName() + " where deleted = 0 order by email desc";
        Query q = entityManager.createQuery(query);
        q.setMaxResults(Math.toIntExact(limit));
        q.setFirstResult(Math.toIntExact(offset));
        return q.getResultList();
    }
}
