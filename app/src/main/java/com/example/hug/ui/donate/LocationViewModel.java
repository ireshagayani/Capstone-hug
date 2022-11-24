package com.example.hug.ui.donate;

public class LocationViewModel {

    String address;
    String city;
    String province;
    String country;
    String postalCode;
    private Integer userId;
    private Integer createdBy;

    public LocationViewModel(Integer userId,Integer createdBy,StringBuilder addressLine1, String addressLine2, String city, String province, String country,
                             String postalCode) {
        this.address = String.valueOf(addressLine1) + " " + addressLine2;
        this.city = city;
        this.province = province;
        this.country = country;
        this.postalCode = postalCode;
        this.userId = userId;
        this.createdBy = createdBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }
}
