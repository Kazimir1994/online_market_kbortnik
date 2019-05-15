package ru.kazimir.bortnik.online_market.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

public class ReviewDTO {
    private UserDTO userDTO;
    @NotNull
    @Min(1)
    private Long id;
    private String feedback;
    private Timestamp dataCreate;
    private boolean showing;

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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "userDTO=" + userDTO +
                ", id=" + id +
                ", feedback='" + feedback + '\'' +
                ", dataCreate=" + dataCreate +
                ", showing=" + showing +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDTO reviewDTO = (ReviewDTO) o;
        return showing == reviewDTO.showing &&
                Objects.equals(userDTO, reviewDTO.userDTO) &&
                Objects.equals(id, reviewDTO.id) &&
                Objects.equals(feedback, reviewDTO.feedback) &&
                Objects.equals(dataCreate, reviewDTO.dataCreate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDTO, id, feedback, dataCreate, showing);
    }
}
