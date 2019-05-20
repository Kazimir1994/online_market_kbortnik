package ru.kazimir.bortnik.online_market.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.kazimir.bortnik.online_market.repository.ReviewRepository;
import ru.kazimir.bortnik.online_market.repository.exception.ReviewRepositoryException;
import ru.kazimir.bortnik.online_market.repository.model.Review;
import ru.kazimir.bortnik.online_market.repository.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.DELETE_REVIEWS_FAILED;
import static ru.kazimir.bortnik.online_market.repository.exception.messageexception.ErrorMessagesRepository.ERROR_QUERY_FAILED;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {
    private static final Logger logger = LoggerFactory.getLogger(ReviewRepositoryImpl.class);

    @Override
    public List<Review> getReviews(Connection connection, Long limitPositions, Long positions) {
        String sqlQuery = "SELECT Reviews.id, Reviews.review,Reviews.data_create,Reviews.showing, User.name,User.surname ,User.patronymic" +
                " FROM Reviews JOIN User ON Reviews.user_id=User.id WHERE Reviews.deleted=FALSE LIMIT ? OFFSET ?";
        List<Review> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, limitPositions);
            preparedStatement.setLong(2, positions);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    userList.add(buildingReview(resultSet));
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
        return userList;
    }

    @Override
    public Long getCountOfReview(Connection connection) {
        String sqlQuery = "SELECT COUNT(*) AS number_of_reviews FROM Reviews WHERE Reviews.deleted = FALSE";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
                if (resultSet.next()) {
                    return resultSet.getLong("number_of_reviews");
                }
                throw new ReviewRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }

    @Override
    public void deleteReviewsById(Connection connection, Long id) {
        String sqlQuery = "UPDATE Reviews SET deleted = TRUE  WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setLong(1, id);
            int sizeUpdate = preparedStatement.executeUpdate();
            if (sizeUpdate > 0) {
                logger.info("Review with ID = {}, was successfully deleted", id);
            } else {
                throw new ReviewRepositoryException(String.format(DELETE_REVIEWS_FAILED, id));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }

    @Override
    public void updateShowing(Connection connection, Review review) {
        String sqlQuery = "UPDATE Reviews SET showing = ?  WHERE id = ? AND showing <> ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setBoolean(1, review.isShowing());
            preparedStatement.setLong(2, review.getId());
            preparedStatement.setBoolean(3, review.isShowing());
            int sizeUpdate = preparedStatement.executeUpdate();
            if (sizeUpdate > 0) {
                logger.info("Review with ID = {}, get showing status = {}", review.getId(), review.isShowing());
            } else {
                logger.info("The status of the show remains the same.");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new ReviewRepositoryException(String.format(ERROR_QUERY_FAILED, sqlQuery), e);
        }
    }

    private Review buildingReview(ResultSet resultSet) throws SQLException {
        Review review = new Review();
        review.setId(resultSet.getLong("id"));
        review.setFeedback(resultSet.getString("review"));
        review.setShowing(resultSet.getBoolean("showing"));
        review.setDataCreate(resultSet.getTimestamp("data_create"));
        User user = new User();
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));
        user.setPatronymic(resultSet.getString("patronymic"));
        review.setUser(user);
        return review;
    }
}
