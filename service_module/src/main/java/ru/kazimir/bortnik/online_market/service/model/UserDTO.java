package ru.kazimir.bortnik.online_market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_NAME;
import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_SURNAME;

public class UserDTO {
    private Long id;

    @NotNull
    @Pattern(regexp = REGEX_NAME, message = "{user.error.user.name}")
    @Size(min = 2, max = 20, message = "{user.error.name.size}")
    private String name;

    @NotNull
    @Size(min = 2, max = 40, message = "{user.error.surname.size}")
    @Pattern(regexp = REGEX_SURNAME, message = "{user.error.user.surname}")
    private String surname;

    private String email;
    private boolean isDeleted;
    private boolean unchangeable;
    private String password;
    private RoleDTO roleDTO;
    private ProfileDTO profileDTO;

    public UserDTO(Long id) {
        this.id = id;
    }

    public UserDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isUnchangeable() {
        return unchangeable;
    }

    public void setUnchangeable(boolean unchangeable) {
        this.unchangeable = unchangeable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleDTO getRoleDTO() {
        return roleDTO;
    }

    public void setRoleDTO(RoleDTO roleDTO) {
        this.roleDTO = roleDTO;
    }

    public ProfileDTO getProfileDTO() {
        return profileDTO;
    }

    public void setProfileDTO(ProfileDTO profileDTO) {
        this.profileDTO = profileDTO;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", isDeleted=" + isDeleted +
                ", unchangeable=" + unchangeable +
                ", password='" + password + '\'' +
                ", roleDTO=" + roleDTO +
                ", profileDTO=" + profileDTO +
                '}';
    }
}