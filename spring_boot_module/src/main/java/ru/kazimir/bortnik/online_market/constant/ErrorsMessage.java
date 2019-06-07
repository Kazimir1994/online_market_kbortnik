package ru.kazimir.bortnik.online_market.constant;

public class ErrorsMessage {

    private ErrorsMessage() {
    }

    public static final String ERROR_UPDATE_PASSWORD_USER_BY_EMAIL = "The password does not match the template or the User with such a password no longer exists.";
    public static final String ERROR_ADD_USER = "Cannot add user. Incorrect data.";
    public static final String ERROR_UPDATE_ROLE = "The role cannot be updated because the role no longer exists.";

    public static final String ERROR_DELETE_REVIEWS = "Incorrect data to delete.";
    public static final String ERROR_UPDATE_STATUS_SHOWING_REVIEWS = "Incorrect data to update status showing.";

    public static final String ERROR_UNAUTHORIZED_USER = "Attempt to get unauthorized access to the resource.";
    public static final String ERROR_GET_UNAUTHORIZED_USER = "No user found in current session.";
    public static final String ERROR_UPDATE_PROFILE = "Incorrect data for profile changes";
    public static final String ERROR_ADD_REVIEW = "Incorrect data review";
}
