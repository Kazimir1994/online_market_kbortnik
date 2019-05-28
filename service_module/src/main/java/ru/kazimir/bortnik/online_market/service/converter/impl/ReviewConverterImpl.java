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
    private final Converter<UserDTO, User> authorConverter;

    public ReviewConverterImpl(@Qualifier("authorConverterImpl") Converter<UserDTO, User> authorConverter) {
        this.authorConverter = authorConverter;
    }

    @Override
    public ReviewDTO toDTO(Review role) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(role.getId());
        reviewDTO.setReview(role.getReview());
        reviewDTO.setDataCreate(role.getDataCreate());
        reviewDTO.setHidden(role.isHidden());
        reviewDTO.setUserDTO(authorConverter.toDTO(role.getUser()));
        return reviewDTO;
    }

    @Override
    public Review fromDTO(ReviewDTO roleDTO) {
        Review review = new Review();
        review.setId(roleDTO.getId());
        review.setReview(roleDTO.getReview());
        review.setDataCreate(roleDTO.getDataCreate());
        review.setHidden(roleDTO.isHidden());
        review.setUser(authorConverter.fromDTO(roleDTO.getUserDTO()));
        return review;
    }
}
