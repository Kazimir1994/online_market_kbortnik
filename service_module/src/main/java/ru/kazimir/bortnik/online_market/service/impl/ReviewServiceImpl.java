package ru.kazimir.bortnik.online_market.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kazimir.bortnik.online_market.repository.ReviewRepository;
import ru.kazimir.bortnik.online_market.repository.UserRepository;
import ru.kazimir.bortnik.online_market.repository.model.Review;
import ru.kazimir.bortnik.online_market.repository.model.User;
import ru.kazimir.bortnik.online_market.service.ReviewService;
import ru.kazimir.bortnik.online_market.service.converter.Converter;
import ru.kazimir.bortnik.online_market.service.exception.ReviewServiceException;
import ru.kazimir.bortnik.online_market.service.model.PageDTO;
import ru.kazimir.bortnik.online_market.service.model.ReviewDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kazimir.bortnik.online_market.service.constans.MessagesLogger.MESSAGES_GET_REVIEWS;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.REVIEW_ERROR_DELETE;
import static ru.kazimir.bortnik.online_market.service.exception.messageexception.ErrorMessagesService.REVIEW_ERROR_UPDATE_SHOWING;

@Service
public class ReviewServiceImpl implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);
    private final ReviewRepository reviewRepository;
    private final Converter<ReviewDTO, Review> reviewConverter;
    private final UserRepository userRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             Converter<ReviewDTO, Review> reviewConverter,
                             UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewConverter = reviewConverter;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public PageDTO<ReviewDTO> getReviews(Long limitPositions, Long currentPage) {
        PageDTO<ReviewDTO> pageDTO = new PageDTO<>();
        Long countOfUsers = reviewRepository.getCountOfEntities();
        Long countOfPages = calculationCountOfPages(countOfUsers, limitPositions);
        pageDTO.setCountOfPages(countOfPages);

        if (currentPage > countOfPages) {
            currentPage = countOfPages;
        } else if (currentPage < countOfPages) {
            currentPage = 1L;
        }
        Long offset = getPosition(limitPositions, currentPage);
        logger.info(MESSAGES_GET_REVIEWS, limitPositions, offset);
        List<Review> userList = reviewRepository.findAll(offset, limitPositions);
        List<ReviewDTO> reviewDTOList = userList.stream().map(reviewConverter::toDTO).collect(Collectors.toList());
        pageDTO.setList(reviewDTOList);
        return pageDTO;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Review review = reviewRepository.findById(id);
        if (review != null) {
            reviewRepository.remove(review);
        } else {
            throw new ReviewServiceException(String.format(REVIEW_ERROR_DELETE, id));
        }
    }

    @Override
    @Transactional
    public void updateHidden(List<ReviewDTO> reviewDTOS) {
        reviewDTOS.forEach(reviewDTO -> {
            Review review = reviewRepository.findById(reviewDTO.getId());
            if (review != null) {
                if (review.isHidden() != reviewDTO.isHidden()) {
                    review.setHidden(reviewDTO.isHidden());
                    reviewRepository.merge(review);
                }
            } else {
                throw new ReviewServiceException(REVIEW_ERROR_UPDATE_SHOWING);
            }
        });
    }

    @Override
    @Transactional
    public void add(ReviewDTO reviewDTO) {
        Review review = new Review();
        User user = userRepository.findById(reviewDTO.getUserDTO().getId());
        review.setReview(reviewDTO.getReview());
        review.setDataCreate(new Timestamp(System.currentTimeMillis()));
        review.setUser(user);
        reviewRepository.persist(review);
    }


    private Long getPosition(Long limitPositions, Long positions) {
        if (positions == 0) {
            positions++;
        }
        return limitPositions * (positions - 1);
    }


    private Long calculationCountOfPages(Long countOfUsers, Long maxPositions) {
        Long countOfPages;
        if (countOfUsers % maxPositions > 0) {
            countOfPages = (countOfUsers / maxPositions) + 1;
        } else {
            countOfPages = countOfUsers / maxPositions;
        }
        return countOfPages;
    }
}
