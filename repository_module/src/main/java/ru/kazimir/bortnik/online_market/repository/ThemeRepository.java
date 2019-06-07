package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.Theme;

public interface ThemeRepository extends GenericRepository<Long, Theme> {

    Theme getByName(String name);
}
