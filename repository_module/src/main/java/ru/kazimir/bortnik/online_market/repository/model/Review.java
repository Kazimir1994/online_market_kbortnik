package ru.kazimir.bortnik.online_market.repository.model;

import java.sql.Timestamp;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return showing == review.showing &&
                Objects.equals(feedback, review.feedback) &&
                Objects.equals(dataCreate, review.dataCreate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(feedback, dataCreate, showing);
    }
}
