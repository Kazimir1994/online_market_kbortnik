package ru.kazimir.bortnik.online_market.service.model;

import java.util.Objects;

public class ProfileDTO {
    private String telephone;
    private String residentialAddress;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(String residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDTO that = (ProfileDTO) o;
        return Objects.equals(telephone, that.telephone) &&
                Objects.equals(residentialAddress, that.residentialAddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(telephone, residentialAddress);
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "telephone='" + telephone + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                '}';
    }
}
