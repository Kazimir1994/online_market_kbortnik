package ru.kazimir.bortnik.online_market.service;

import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import java.util.List;

public interface ReviewService {

    PageDTO<ReviewDTO> getReviews(Long limitPositions, Long currentPage);

    void deleteById(Long id);

    void updateHidden(List<ReviewDTO> reviewDTOS);

    void add(ReviewDTO reviewDTO);
}
