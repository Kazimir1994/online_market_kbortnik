package ru.kazimir.bortnik.online_market.service.model;

import java.util.Objects;

public class RoleDTO {
    private String name;
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleDTO roleDTO = (RoleDTO) o;
        return Objects.equals(name, roleDTO.name) &&
                Objects.equals(id, roleDTO.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, id);
    }
}
