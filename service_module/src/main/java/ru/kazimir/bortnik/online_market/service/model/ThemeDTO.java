package ru.kazimir.bortnik.online_market.service.model;

public class ThemeDTO {
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
