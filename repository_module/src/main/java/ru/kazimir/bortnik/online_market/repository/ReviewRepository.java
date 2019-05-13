package ru.kazimir.bortnik.online_market.repository;

import ru.kazimir.bortnik.online_market.repository.model.Review;

import java.sql.Connection;
import java.util.List;

public interface ReviewRepository extends GenericRepository {
    List<Review> getReviews(Connection connection, Long limitPositions, Long positions);

    Long getSizeData(Connection connection);

    void deleteReviewsById(Connection connection, Long id);

    void updateShowing(Connection connection, Review review);
}