package com.softkali.buddhasuddha.dashboard.model;
import java.time.LocalDateTime;


public class Product {
    private Long id;
    private String productId;

    private String productName;
    private String productDescription;
    private String productRate;
    private String productDeliverCharge;

    private String productImageLink;

    private String createdTime;
    private String productType;
    private String category;
    private String location;

    public Product() {
    }

    public Product(Long id, String productId, String productName, String productDescription, String productRate, String productDeliverCharge, String productImageLink, String createdTime, String productType, String category, String location) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productRate = productRate;
        this.productDeliverCharge = productDeliverCharge;
        this.productImageLink = productImageLink;
        this.createdTime = createdTime;
        this.productType = productType;
        this.category = category;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductRate() {
        return productRate;
    }

    public void setProductRate(String productRate) {
        this.productRate = productRate;
    }

    public String getProductDeliverCharge() {
        return productDeliverCharge;
    }

    public void setProductDeliverCharge(String productDeliverCharge) {
        this.productDeliverCharge = productDeliverCharge;
    }

    public String getProductImageLink() {
        return productImageLink;
    }

    public void setProductImageLink(String productImageLink) {
        this.productImageLink = productImageLink;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
