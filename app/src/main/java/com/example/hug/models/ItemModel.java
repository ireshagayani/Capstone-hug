package com.example.hug.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import kotlinx.coroutines.channels.ActorKt;

public class ItemModel {


    @SerializedName("id")
    private int Id;

    @SerializedName("name")
    private String Name;

    @SerializedName("qty")
    private int Qty;

    @SerializedName("itemStatus")
    private String ItemStatus;

    @SerializedName("locationId")
    private int LocationId;

    @SerializedName("createdDateString")
    private String CreatedDateString;

    @SerializedName("modifiedDateString")
    private String ModifiedDateString;

    public String getPickupDateTimeString() {
        return PickupDateTimeString;
    }

    @SerializedName("location")
    private LocationModel locationModel;

    @SerializedName("userId")
    private Integer UserId;

    public Integer getUserId() {
        return UserId;
    }

    public void setUserId(Integer userId) {
        UserId = userId;
    }

    public LocationModel getLocationModel() {
        return locationModel;
    }

    public void setLocationModel(LocationModel locationModel) {
        this.locationModel = locationModel;
    }

    public void setPickupDateTimeString(String pickupDateTimeString) {
        PickupDateTimeString = pickupDateTimeString;
    }

    @SerializedName("pickupDateTimeString")
    private String PickupDateTimeString;

    @SerializedName("pickupDateTime")
    private String PickupDateTime;

    public String getPickupDateTime() {
        return PickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        PickupDateTime = pickupDateTime;
    }

    @SerializedName("pickupInstruction")
    private String PickupInstruction;

//    @SerializedName("createdDate")
//    private Date CreatedDate;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getItemStatus() {
        return ItemStatus;
    }

    public void setItemStatus(String itemStatus) {
        ItemStatus = itemStatus;
    }

    public int getLocationId() {
        return LocationId;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

//    public Date getPickupDateTime() {
//        return PickupDateTime;
//    }
//
//    public void setPickupDateTime(Date pickupDateTime) {
//        PickupDateTime = pickupDateTime;
//    }

    public String getPickupInstruction() {
        return PickupInstruction;
    }

    public void setPickupInstruction(String pickupInstruction) {
        PickupInstruction = pickupInstruction;
    }

//    public Date getCreatedDate() {
//        return CreatedDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        CreatedDate = createdDate;
//    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCreatedDateString() {
        return CreatedDateString;
    }

    public void setCreatedDateString(String createdDateString) {
        CreatedDateString = createdDateString;
    }

    public String getModifiedDateString() {
        return ModifiedDateString;
    }

    public void setModifiedDateString(String modifiedDateString) {
        ModifiedDateString = modifiedDateString;
    }
}
