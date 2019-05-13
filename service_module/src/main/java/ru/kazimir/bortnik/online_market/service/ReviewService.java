package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getReviews(Long limitPositions, Long positions);

    Long getNumberOfPages(Long maxPositions);

    void deleteReviewsById(Long id);

    void updateShowing(List<ReviewDTO> reviewDTOS);
}
