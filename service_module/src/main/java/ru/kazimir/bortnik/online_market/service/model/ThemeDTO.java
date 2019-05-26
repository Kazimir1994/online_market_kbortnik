package ru.kazimir.bortnik.online_market.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ThemeDTO {
    @NotNull
    @Min(1)
    private Long id;
    private String name;

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

    @Override
    public String toString() {
        return "ThemeDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
