package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.ThemeRepository;
import ru.kazimir.bortnik.online_market.repository.model.Theme;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class ThemeRepositoryImpl extends GenericRepositoryImpl<Long, Theme> implements ThemeRepository {

    @Override
    public Theme getByName(String name) {
        String queryString = "from " + Theme.class.getName() + " where name =:name ";
        Query query = entityManager.createQuery(queryString).setParameter("name", name);
        try {
            return (Theme) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
