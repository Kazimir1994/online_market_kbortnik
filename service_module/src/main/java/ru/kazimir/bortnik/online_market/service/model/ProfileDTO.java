package ru.kazimir.bortnik.online_market.service.model;

public class ProfileDTO {
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "id=" + id +
                ", telephone='" + telephone + '\'' +
                ", residentialAddress='" + residentialAddress + '\'' +
                '}';
    }
}
