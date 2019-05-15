package ru.kazimir.bortnik.online_market.repository.model;

import java.sql.Timestamp;

public class Review extends Model {
    private User user;
    private String feedback;
    private Timestamp dataCreate;
    private boolean showing;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Timestamp getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(Timestamp dataCreate) {
        this.dataCreate = dataCreate;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "Review{" +
                "user=" + user +
                ", feedback='" + feedback + '\'' +
                ", timestamp=" + dataCreate.toString() +
                ", showing=" + showing +
                '}';
    }

}
