package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.Item;

import java.util.List;

public interface ItemRepository extends GenericRepository<Long, Item> {
    List<Item> findAll(Long offset, Long limit);
}
