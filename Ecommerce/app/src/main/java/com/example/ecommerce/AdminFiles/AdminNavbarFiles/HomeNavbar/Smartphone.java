package com.example.ecommerce.AdminFiles.AdminNavbarFiles.HomeNavbar;

import java.io.Serializable;

public class Smartphone  implements Serializable {
    private String productId;
    private String name;
    private String description;
    private String price;
    private String imageUrl;



    public Smartphone(){

    }

    public Smartphone(String productId, String name, String description, String price, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getProductId() {

        return productId;
    }

    public void setProductId(String productId) {

        this.productId = productId;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {

        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

