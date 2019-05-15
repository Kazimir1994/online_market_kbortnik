package ru.kazimir.bortnik.online_market.repository;

import java.sql.Connection;

public interface GenericRepository {

    Connection getConnection();
}
