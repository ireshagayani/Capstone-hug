package com.example.hug.models;

import androidx.lifecycle.ViewModel;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class LocationModel  extends ViewModel {

    @SerializedName("id")
    private Integer Id;

    @SerializedName("userId")
    private Integer UserId;

    @SerializedName("name")
    private String Name;

    @SerializedName("address")
    private String Address;

    @SerializedName("city")
    private String City;

    @SerializedName("province")
    private String Province;

    @SerializedName("postalCode")
    private String PostalCode;

    @SerializedName("country")
    private String Country;

    @SerializedName("phone")
    private String Phone;

    @SerializedName("locationType")
    private String LocationType;

    @SerializedName("createdBy")
    private Integer CreatedBy;

    @SerializedName("modifiedBy")
    private Integer ModifiedBy;

    public Integer getId() { return Id; }
    public void setId(Integer id) { Id = id; }

    public Integer getUserId() { return UserId; }
    public void setUserId(Integer userId) { UserId = userId; }

    public String getName() { return Name; }
    public void setName(String name) { Name = name; }

    public String getAddress() { return Address; }
    public void setAddress(String address) { Address = address; }

    public String getCity() { return City; }
    public void setCity(String city) { City = city; }

    public String getProvince() { return Province; }
    public void setProvince(String province) { Province = province; }

    public String getPostalCode() { return PostalCode; }
    public void setPostalCode(String postalCode) { PostalCode = postalCode; }

    public String getCountry() { return Country; }
    public void setCountry(String country) { Country = country; }

    public String getPhone() { return Phone; }
    public void setPhone(String phone) { Phone = phone; }

    public String getLocationType() { return LocationType; }
    public void setLocationType(String locationType) { LocationType = locationType; }

    public Integer getCreatedBy() { return CreatedBy; }
    public void setCreatedBy(Integer createdBy) { CreatedBy = createdBy; }

    public Integer getModifiedBy() { return ModifiedBy; }
    public void setModifiedBy(Integer modifiedBy) { ModifiedBy = modifiedBy; }
}
