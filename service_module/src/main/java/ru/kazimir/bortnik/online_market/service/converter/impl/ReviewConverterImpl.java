package ru.kazimir.bortnik.online_market.service.converter.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.kazimir.bortnik.online_market.repository.model.Review;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;
import ru.kazimir.bortnik.online_market.service.model.UserDTO;

@Component
public class ReviewConverterImpl implements Converter<ReviewDTO, Review> {
    private final Converter<UserDTO, User> userConverter;

    public ReviewConverterImpl(@Qualifier("userReviewConverterImpl") Converter<UserDTO, User> userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public ReviewDTO toDTO(Review role) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(role.getId());
        reviewDTO.setFeedback(role.getFeedback());
        reviewDTO.setDataCreate(role.getDataCreate());
        reviewDTO.setShowing(role.isShowing());
        reviewDTO.setUserDTO(userConverter.toDTO(role.getUser()));
        return reviewDTO;
    }

    @Override
    public Review fromDTO(ReviewDTO roleDTO) {
        Review review = new Review();
        review.setId(roleDTO.getId());
        review.setFeedback(roleDTO.getFeedback());
        review.setDataCreate(roleDTO.getDataCreate());
        review.setShowing(roleDTO.isShowing());
        review.setUser(userConverter.fromDTO(roleDTO.getUserDTO()));
        return review;
    }
}
