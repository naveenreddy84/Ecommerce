package com.example.ecommerce.users;



public class Order {
    private String orderStatus;
    private String productId;
    private String productName;
    private String productPrice;
    private String quantity;
    private String userId;
    private String productImage;


    public Order() {

    }

    public Order(String orderStatus, String productId, String productName, String productPrice,
                 String quantity, String userId, String productImage) {
        this.orderStatus = orderStatus;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.userId = userId;
        this.productImage = productImage;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductImage() {
        return productImage;
    }
}
