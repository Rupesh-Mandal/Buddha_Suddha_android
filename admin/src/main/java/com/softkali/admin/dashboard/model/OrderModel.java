package com.softkali.admin.dashboard.model;

public class OrderModel {
    private Long id;

    private String orderId;

    private String userId;
    private String pickupBoyId;
    private boolean pickupBoyStatus;
    private boolean isRunning;

    private String name;
    private String phoneNumber;
    private String productDeliverAddress;
    private String location;

    private String orderData;

    private String totalRate;

    private String status;
    private String statusMessage;
    private String createdTime;

    public OrderModel() {
    }

    public OrderModel(Long id, String orderId, String userId, String pickupBoyId, boolean pickupBoyStatus, boolean isRunning, String name, String phoneNumber, String productDeliverAddress, String location, String orderData, String totalRate, String status, String statusMessage, String createdTime) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.pickupBoyId = pickupBoyId;
        this.pickupBoyStatus = pickupBoyStatus;
        this.isRunning = isRunning;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.productDeliverAddress = productDeliverAddress;
        this.location = location;
        this.orderData = orderData;
        this.totalRate = totalRate;
        this.status = status;
        this.statusMessage = statusMessage;
        this.createdTime = createdTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPickupBoyId() {
        return pickupBoyId;
    }

    public void setPickupBoyId(String pickupBoyId) {
        this.pickupBoyId = pickupBoyId;
    }

    public boolean isPickupBoyStatus() {
        return pickupBoyStatus;
    }

    public void setPickupBoyStatus(boolean pickupBoyStatus) {
        this.pickupBoyStatus = pickupBoyStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProductDeliverAddress() {
        return productDeliverAddress;
    }

    public void setProductDeliverAddress(String productDeliverAddress) {
        this.productDeliverAddress = productDeliverAddress;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}
