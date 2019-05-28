package ru.kazimir.bortnik.online_market.repository.impl;

import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.ReviewRepository;
import ru.kazimir.bortnik.online_market.repository.model.Review;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
}
