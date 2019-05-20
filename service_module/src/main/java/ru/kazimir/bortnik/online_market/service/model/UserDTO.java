package ru.kazimir.bortnik.online_market.service.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_NAME;
import static ru.kazimir.bortnik.online_market.service.constans.ConstantValidationJAR.REGEX_SURNAME;

public class UserDTO {
    @NotNull
    @Pattern(regexp = REGEX_NAME, message = "{user.error.user.name}")
    @Size(min = 2, max = 20, message = "{user.error.name.size}")
    private String name;

    @NotNull
    @Size(min = 2, max = 40, message = "{user.error.surname.size}")
    @Pattern(regexp = REGEX_SURNAME, message = "{user.error.user.surname}")
    private String surname;

    private String patronymic;
    private String email;
    private Long id;
    private ProfileDTO profileDTO;
    private RoleDTO roleDTO;
    private boolean canBeRemoved;
    private String password;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleDTO getRoleDTO() {
        return roleDTO;
    }

    public void setRoleDTO(RoleDTO roleDTO) {
        this.roleDTO = roleDTO;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public boolean isCanBeRemoved() {
        return canBeRemoved;
    }

    public void setCanBeRemoved(boolean canBeRemoved) {
        this.canBeRemoved = canBeRemoved;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", profileDTO=" + profileDTO +
                ", roleDTO=" + roleDTO +
                ", canBeRemoved=" + canBeRemoved +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return canBeRemoved == userDTO.canBeRemoved &&
                Objects.equals(name, userDTO.name) &&
                Objects.equals(surname, userDTO.surname) &&
                Objects.equals(patronymic, userDTO.patronymic) &&
                Objects.equals(email, userDTO.email) &&
                Objects.equals(profileDTO, userDTO.profileDTO) &&
                Objects.equals(id, userDTO.id) &&
                Objects.equals(roleDTO, userDTO.roleDTO) &&
                Objects.equals(password, userDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic, email, profileDTO, id, roleDTO, canBeRemoved, password);
    }
}