package ru.kazimir.bortnik.online_market.repository.impl;

import ru.kazimir.bortnik.online_market.repository.GenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[1];
    }

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void merge(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public T findById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<T> findAll() {
        String query = "from " + entityClass.getName() + " c";
        Query q = entityManager.createQuery(query);
        return q.getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<T> findAll(Long offset, Long limit) {
        String query = "from " + entityClass.getName() + " c";
        Query q = entityManager.createQuery(query);
        q.setMaxResults(Math.toIntExact(limit));
        q.setFirstResult(Math.toIntExact(offset));
        return q.getResultList();
    }

    @Override
    public Long getCountOfEntities() {
        String query = "SELECT COUNT(*) FROM " + entityClass.getName() + " c";
        Query q = entityManager.createQuery(query);
        return ((Number) q.getSingleResult()).longValue();
    }
}
