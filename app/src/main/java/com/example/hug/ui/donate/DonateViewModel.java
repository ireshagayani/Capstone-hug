package com.example.hug.ui.donate;

import androidx.lifecycle.ViewModel;

public class DonateViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private String name;
    private Integer qty;
    private LocationViewModel location;
    private String pickupDateTime;
    private String pickupInstruction;
    private String itemStatus;
    private Integer userId;
    private Integer createdBy;

    public DonateViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPickupInstruction() {
        return pickupInstruction;
    }

    public void setPickupInstruction(String pickupInstruction) {
        this.pickupInstruction = pickupInstruction;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public LocationViewModel getLocation() {
        return location;
    }

    public void setLocation(LocationViewModel location) {
        this.location = location;
    }

    public String getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
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