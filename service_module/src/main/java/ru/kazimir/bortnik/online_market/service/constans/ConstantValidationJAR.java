package ru.kazimir.bortnik.online_market.service.constans;

public class ConstantValidationJAR {

    private ConstantValidationJAR() {
    }

    public static final String REGEX_EMAIL = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
    public static final String REGEX_TELEPHONE = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$";
    public static final String REGEX_NAME = "^[a-zA-Z]{2,20}$";
    public static final String REGEX_SURNAME = "^[a-zA-Z]{3,40}$";
    public static final String REGEX_PATRONYMIC = "^[a-zA-Z]{0,40}$";
    public static int DEFAULT_SIZE_OF_PASSWORDS = 10;
}
