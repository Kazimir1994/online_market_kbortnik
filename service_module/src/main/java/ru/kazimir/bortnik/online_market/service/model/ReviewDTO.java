package ru.kazimir.bortnik.online_market.service.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class ReviewDTO {
    private Long id;
    @Length(min = 10, max = 1000, message = "{review.error.review.size}")
    private String review;
    private Timestamp dataCreate;
    private UserDTO userDTO;
    private boolean hidden;

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public Timestamp getDataCreate() {
        return dataCreate;
    }

    public void setDataCreate(Timestamp dataCreate) {
        this.dataCreate = dataCreate;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "userDTO=" + userDTO +
                ", id=" + id +
                ", review='" + review + '\'' +
                ", dataCreate=" + dataCreate +
                ", hidden=" + hidden +
                '}';
    }
}
