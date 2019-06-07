package ru.kazimir.bortnik.online_market.service.constans;

public class MessagesLogger {

    private MessagesLogger() {
    }

    public static final String MESSAGES_GET_PROFILE = "By ID :={}, got the following profile := {}.";
    public static final String MESSAGES_GET_PASSWORD = "By ID :={}, Password := {}.";
    public static final String MESSAGES_UPDATE_PROFILE = "profile for changes := {}.";
    public static final String MESSAGES_GET_ARTICLE = "Worth list of articles by the following parameters:" +
            " Amount of news := {}, From the position := {}, Op theme := {}";
    public static final String MESSAGES_GET_ARTICLE_TOP = "Worth list of articles by the following parameters:" +
            " Amount of news := {}, SizeTop := {}";
    public static final String MESSAGES_GET_THEME = "List theme : = {}";
    public static final String MESSAGES_GET_ARTICLES = "Worth list of articles by the following parameters:" +
            "Amount of news := {}, limit:={} ,offset := {}.";
    public static final String MESSAGES_GET_USERS = "List of users by the following parameters:" +
            " Amount of Users := {}, From the position := {}.";
    public static final String MESSAGES_GET_REVIEWS = "List of reviews by the following parameters:" +
            " Amount of Reviews := {}, From the position := {}.";
    public static final String MESSAGES_GET_ITEMS = "List of items by the following parameters:" +
            " Amount of items := {}, From the position := {}.";
    public static final String MESSAGES_GET_ORDERS = "List of items by the following parameters:" +
            " Amount of Orders := {}, From the position := {}, ID user:= {}";
}
