package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kazimir.bortnik.online_market.repository.ReviewRepository;
import ru.kazimir.bortnik.online_market.repository.exception.ConnectionDataBaseExceptions;
import ru.kazimir.bortnik.online_market.repository.exception.ReviewRepositoryException;
import ru.kazimir.bortnik.online_market.repository.model.Review;
import ru.kazimir.bortnik.online_market.service.ReviewService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.ReviewServiceException;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.*;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final Converter<ReviewDTO, Review> reviewConverter;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, Converter<ReviewDTO, Review> reviewConverter) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
    }

    @Override
    public List<ReviewDTO> getReviews(Long limitPositions, Long positions) {
        try (Connection connection = reviewRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<Review> userList = reviewRepository.getReviews(connection, limitPositions, limitPositions * (positions - 1));
                List<ReviewDTO> reviewDTOList = userList.stream().map(reviewConverter::toDTO).collect(Collectors.toList());
                connection.commit();
                return reviewDTOList;
            } catch (SQLException | ReviewRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(REVIEW_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public Long getNumberOfPages(Long maxPositions) {
        try (Connection connection = reviewRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                Long sizeData = reviewRepository.getSizeData(connection);
                if (sizeData % maxPositions > 0) {
                    sizeData = sizeData / maxPositions;
                    sizeData++;
                } else {
                    sizeData = sizeData / maxPositions;
                    if (sizeData == 0) {
                        sizeData++;
                    }
                }
                return sizeData;
            } catch (SQLException | ReviewRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(NUMBER_OF_PAGES, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void deleteReviewsById(Long id) {
        try (Connection connection = reviewRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                reviewRepository.deleteReviewsById(connection, id);
                connection.commit();
            } catch (SQLException | ReviewRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(String.format(REVIEW_ERROR_DELETE, id), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void updateShowing(List<ReviewDTO> reviewDTOS) {
        try (Connection connection = reviewRepository.getConnection()) {
            try {
                connection.setAutoCommit(false);
                List<Review> reviewList = reviewDTOS.stream().map(reviewConverter::fromDTO).collect(Collectors.toList());
                reviewList.forEach(review -> reviewRepository.updateShowing(connection, review));
                connection.commit();
            } catch (SQLException | ReviewRepositoryException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new ReviewServiceException(REVIEW_ERROR_UPDATE_SHOWING, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ConnectionDataBaseExceptions(CONNECTION_ERROR_MESSAGE, e);
        }
    }
}
