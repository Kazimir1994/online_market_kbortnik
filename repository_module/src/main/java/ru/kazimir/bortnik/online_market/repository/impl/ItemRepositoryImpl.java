package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.ItemRepository;
import ru.kazimir.bortnik.online_market.repository.model.Item;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ItemRepositoryImpl extends GenericRepositoryImpl<Long, Item> implements ItemRepository {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<Item> findAll(Long offset, Long limit) {
        String query = "from " + Item.class.getName() + " where deleted = 0 order by name ";
        Query q = entityManager.createQuery(query)
                .setMaxResults(Math.toIntExact(limit))
                .setFirstResult(Math.toIntExact(offset));
        return q.getResultList();
    }
}
