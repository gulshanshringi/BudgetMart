package com.jsrd.budgetmart.model;

public class Cart {
    private String cartId;
    private Product product;
    private String quantity;

    public Cart(String cartId, Product product, String quantity) {
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

    public String getQuantity() {
        return quantity;
    }
}
