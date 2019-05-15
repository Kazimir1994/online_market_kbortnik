package ru.kazimir.bortnik.online_market.repository.exception.messageexception;

public class ErrorMessagesRepository {
    private ErrorMessagesRepository() {
    }

    public static final String ERROR_QUERY_FAILED = "Query: %s - failed.";
    public static final String NO_CONNECTION = "Failed to get connection data base.";
    public static final String DELETE_USER_FAILED = "Problems occurred when deleting a user with ID = %d.";
    public static final String ADD_USER_FILED = "Error adding user %s.";
    public static final String UPDATE_ROLE_FAILED = "when changing roles to role = %s, user with ID = %d had problems.";
    public static final String UPDATE_PASSWORD_FAILED = "Fail update user's( Email= %s ) password.";
    public static final String DELETE_REVIEWS_FAILED = "Problems occurred when deleting a review with ID = %d)";

}
