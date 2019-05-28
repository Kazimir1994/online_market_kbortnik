package ru.kazimir.bortnik.online_market.service.exception.messageexception;

public class ErrorMessagesService {

    private ErrorMessagesService() {
    }

    public static final String USER_ERROR_BY_EMAIL_MESSAGE = "Error while getting user by email. Email:= %s";
    public static final String USERS_GET_ERROR_MESSAGE = "Error while getting users from data source. Value positions" +
            ":= %d, positions:= %d";

    public static final String USER_ERROR_DELETE = "Error while deleting user. List ID:= %s";
    public static final String USER_ERROR_UPDATE_ROLE = "Error while changing user's role. Name new Role:= %s, ID user:= %d";
    public static final String USER_ERROR_UPDATE_PASSWORD = "Error while changing user's password. Email:= %s";
    public static final String ADD_USER_ERROR_MESSAGE = "Error while adding new user into data source. User:= %s";

    public static final String ROLE_ERROR_MESSAGE = "Error while getting roles from data source.";

    public static final String REVIEW_ERROR_MESSAGE = "Error while getting reviews from data source.";
    public static final String REVIEW_ERROR_UPDATE_SHOWING = "Error while changing status showing.";
    public static final String REVIEW_ERROR_DELETE = "Error deleting reviews. ID:= %d";

    public static final String USER_ERROR_GET_USER = "user by id := %d does not exist.";

    public static final String ERROR_GET_ARTICLE_BY_ID = "News id :=%d found";
    public static final String ERROR_DELETE_ARTICLE_BY_ID = "It is impossible to delete news from id := %d because it no longer exists.";
    public static final String ERROR_DELETE_COMMENT_BY_ID = "It is impossible to delete comment from id := %d because it no longer exists.";

    public static final String USER_ERROR_GET_ITEM = "item by id := %d does not exist.";
}
