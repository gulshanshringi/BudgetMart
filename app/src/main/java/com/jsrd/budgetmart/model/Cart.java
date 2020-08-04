package com.jsrd.budgetmart.model;

public class Cart {
    private String cartId;
    private Product product;
    private int quantity;

    public Cart(String cartId, Product product, int quantity) {
        this.cartId = cartId;
        this.product = product;
        this.quantity = quantity;
    }

    public String getCartId() {
        return cartId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
