package com.softkali.buddhasuddha.dashboard.model;

public class AddressBookModel {

    private Long id;
    private String productDeliverAddress;
    private String userId;
    private String userPhoneNumber;

    public AddressBookModel() {
    }

    public AddressBookModel(Long id, String productDeliverAddress, String userId, String userPhoneNumber) {
        this.id = id;
        this.productDeliverAddress = productDeliverAddress;
        this.userId = userId;
        this.userPhoneNumber = userPhoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductDeliverAddress() {
        return productDeliverAddress;
    }

    public void setProductDeliverAddress(String productDeliverAddress) {
        this.productDeliverAddress = productDeliverAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
