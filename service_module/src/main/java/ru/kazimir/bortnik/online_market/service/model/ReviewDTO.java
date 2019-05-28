package ru.kazimir.bortnik.online_market.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class ReviewDTO {
    @NotNull
    @Min(1)
    private Long id;
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
