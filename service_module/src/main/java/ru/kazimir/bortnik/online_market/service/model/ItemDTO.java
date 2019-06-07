package ru.kazimir.bortnik.online_market.service.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_NUMBER;

public class ItemDTO {
    private Long id;

    @NotNull
    @Length(max = 100, min = 2, message = "{article.error.dataCreate}")
    private String name;

    @NotNull
    @Pattern(regexp = REGEX_NUMBER, message = "{item.error.price}")
    private String price;

    private String uniqueNumber;

    @NotNull
    @Length(max = 200, min = 2, message = "{item.error.description}")
    private String shortDescription;

    private boolean deleted;

    public ItemDTO() {

    }

    public ItemDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(String uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", uniqueNumber='" + uniqueNumber + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
