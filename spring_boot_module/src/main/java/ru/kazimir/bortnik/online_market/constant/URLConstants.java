package ru.kazimir.bortnik.online_market.constant;

public class URLConstants {

    private URLConstants() {
    }

    public static final String ERROR_URL = "/error";
    public static final String ERROR_403_PAGE_URL = "/403";
    public static final String ERROR_403_PAGE = "error/403";
    public static final String ERROR_500_PAGE_URL = "/500";
    public static final String ERROR_500_PAGE = "error/500";
    public static final String ERROR_404_PAGE_URL = "/404";
    public static final String ERROR_404_PAGE = "error/404";
    public static final String ERROR_422_PAGE_URL = "/422";
    public static final String ERROR_422_PAGE = "error/422";
    public static final String REDIRECT_ERROR_422 = "redirect:/error/422";

    public static final String HOME_PAGE_URL = "/home";
    public static final String HOME_PAGE = "home";
    public static final String DEFAULT_PAGE_URL = "/";
    public static final String LOGIN_PAGE_URL = "/login";
    public static final String LOGIN_PAGE = "login";
    public static final String PRIVATE_URL = "/private/**";

    public static final String PRIVATE_REVIEWS_URL = "/private/reviews";
    public static final String PRIVATE_REVIEWS_SHOWING_URL = "/showing";
    public static final String PRIVATE_REVIEWS_PAGE = "private_reviews";
    public static final String PRIVATE_REVIEWS_UPDATE_SHOWING_URL = "/update_showing";
    public static final String PRIVATE_REVIEWS_DELETE_URL = "/delete_reviews";
    public static final String REDIRECT_PRIVATE_REVIEWS_SHOWING = "redirect:/private/reviews/showing";

    public static final String PRIVATE_USERS_PAGE = "private_users";
    public static final String PRIVATE_ADD_USERS_PAGE = "private_addUsers";
    public static final String PRIVATE_USERS_URL = "/private/users";
    public static final String PRIVATE_USERS_SHOWING_URL = "/showing";
    public static final String PRIVATE_ADD_USERS_URL = "/add_users";
    public static final String PRIVATE_ADD_FORM_USERS_URL = "/form_add_users";
    public static final String PRIVATE_CHANGE_PASSWORD_USERS_URL = "/change_password";
    public static final String PRIVATE_UPDATE_ROLE_USERS_URL = "/update_role";
    public static final String PRIVATE_DELETE_USERS_URL = "/delete_users";

    public static final String REDIRECT_USERS_SHOWING_URL = "/private/users/showing";
    public static final String REDIRECT_PRIVATE_USERS_SHOWING = "redirect:/private/users/showing";
    public static final String REDIRECT_PRIVATE_FORM_ADD_USERS = "redirect:/private/users/form_add_users";

    public static final String CSS_CONTENT_URL = "/css/**";

}
