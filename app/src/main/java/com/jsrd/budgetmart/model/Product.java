package com.jsrd.budgetmart.model;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String image;
    private String name;
    private int price;

    public Product(int id, String name, int price, String image) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }

}
