package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.OrderRepository;
import ru.kazimir.bortnik.online_market.repository.model.Item;
import ru.kazimir.bortnik.online_market.repository.model.Order;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    public Long getCountOfOrdersById(Long id) {
        String query = "SELECT COUNT(*) FROM " + Order.class.getName() + " where user.id =:id";
        Query q = entityManager.createQuery(query).setParameter("id", id);
        return ((Number) q.getSingleResult()).longValue();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Order> getOrdersByUserId(Long offset, Long limitPositions, Long id) {
        String query = "from " + Order.class.getName() + " where user.id =:id order by dataCreation desc ";
        Query q = entityManager.createQuery(query).setParameter("id", id);
        q.setMaxResults(Math.toIntExact(limitPositions));
        q.setFirstResult(Math.toIntExact(offset));
        return q.getResultList();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Order> findAll(Long offset, Long limit) {
        String query = "from " + Order.class.getName() + " order by dataCreation desc ";
        Query q = entityManager.createQuery(query)
                .setMaxResults(Math.toIntExact(limit))
                .setFirstResult(Math.toIntExact(offset));
        return q.getResultList();
    }
}
