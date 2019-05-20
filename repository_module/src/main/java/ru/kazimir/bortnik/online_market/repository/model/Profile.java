package ru.kazimir.bortnik.online_market.repository.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Profile")
public class Profile {
    @GenericGenerator(
            name = "generatorUserId",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "user"))

    @Id
    @GeneratedValue(generator = "generatorUserId")
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "address")
    private String residentialAddress;

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private User user;

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", telephone='" + telephone + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id) &&
                Objects.equals(telephone, profile.telephone) &&
                Objects.equals(residentialAddress, profile.residentialAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, telephone, residentialAddress);
    }
}
